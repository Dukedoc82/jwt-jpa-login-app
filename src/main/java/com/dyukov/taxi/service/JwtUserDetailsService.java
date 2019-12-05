package com.dyukov.taxi.service;

import java.util.ArrayList;
import java.util.List;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.AppUser;
import com.dyukov.taxi.entity.ExpiredToken;
import com.dyukov.taxi.repository.ExpiredTokensRepository;
import com.dyukov.taxi.repository.UserDetailsRepository;
import com.dyukov.taxi.repository.UserRoleRepository;
import com.dyukov.taxi.utils.EncryptedPasswordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ExpiredTokensRepository expiredTokensRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userDetailsRepository.findUserAccount(username);

        if (appUser == null) {
            logger.warn("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        List<String> roleNames = this.userRoleRepository.getRoleNames(appUser.getUserId());

        List<GrantedAuthority> grantList = new ArrayList<>();
        if (roleNames != null) {
            for (String role : roleNames) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        return new User(appUser.getUserName(), //
                appUser.getEncrytedPassword(), grantList);

    }

    public UserDao save(RegistrationData registrationData) {
        AppUser appUser = convertFromDto(registrationData);
        AppUser savedUser = userDetailsRepository.saveUser(appUser);
        return convertToDTO(savedUser);
    }

    public UserDao saveAdmin(RegistrationData registrationData) {
        AppUser appUser = convertFromDto(registrationData);
        AppUser savedUser = userDetailsRepository.saveAdmin(appUser);
        return convertToDTO(savedUser);
    }

    public void invalidateToken(String token) {
        ExpiredToken expiredToken = new ExpiredToken();
        expiredToken.setToken(token);
        expiredTokensRepository.invalidateToken(expiredToken);
    }

    private UserDao convertToDTO(AppUser appUser) {
        return modelMapper.map(appUser, UserDao.class);
    }

    private AppUser convertFromDto(RegistrationData registrationData) {
        AppUser appUser = modelMapper.map(registrationData, AppUser.class);
        appUser.setEncrytedPassword(EncryptedPasswordUtils.encryptePassword(registrationData.getPassword()));
        appUser.setEnabled(true);
        return appUser;
    }
}
