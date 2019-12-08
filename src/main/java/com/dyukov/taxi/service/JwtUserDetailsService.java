package com.dyukov.taxi.service;

import java.util.ArrayList;
import java.util.List;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.model.TpUserDetails;
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

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TpUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TpUser tpUser = userDetailsRepository.findUserAccount(username);

        if (tpUser == null) {
            logger.warn("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        List<String> roleNames = this.userRoleRepository.getRoleNames(tpUser.getUserId());

        List<GrantedAuthority> grantList = new ArrayList<>();
        if (roleNames != null) {
            for (String role : roleNames) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        return new TpUserDetails(tpUser.getUserId(), tpUser.getUserName(), //
                tpUser.getEncrytedPassword(), grantList);

    }

    public UserDao save(RegistrationData registrationData) {
        TpUser tpUser = convertFromDto(registrationData);
        TpUser savedUser = userDetailsRepository.saveUser(tpUser);
        return convertToDTO(savedUser);
    }

    public UserDao saveAdmin(RegistrationData registrationData) {
        TpUser tpUser = convertFromDto(registrationData);
        TpUser savedUser = userDetailsRepository.saveAdmin(tpUser);
        return convertToDTO(savedUser);
    }

    public UserDao saveDriver(RegistrationData registrationData) {
        TpUser tpUser = convertFromDto(registrationData);
        TpUser savedUser = userDetailsRepository.saveDriver(tpUser);
        return convertToDTO(savedUser);
    }

    private UserDao convertToDTO(TpUser tpUser) {
        return modelMapper.map(tpUser, UserDao.class);
    }

    private TpUser convertFromDto(RegistrationData registrationData) {
        TpUser tpUser = modelMapper.map(registrationData, TpUser.class);
        tpUser.setEncrytedPassword(EncryptedPasswordUtils.encryptePassword(registrationData.getPassword()));
        tpUser.setEnabled(true);
        return tpUser;
    }
}
