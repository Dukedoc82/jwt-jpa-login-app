package com.dyukov.taxi.service;

import java.util.ArrayList;
import java.util.List;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.AppUser;
import com.dyukov.taxi.repository.UserDetailsRepository;
import com.dyukov.taxi.repository.UserRoleRepository;
import com.dyukov.taxi.utils.EncryptedPasswordUtils;
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

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userDetailsRepository.findUserAccount(username);

        if (appUser == null) {
            System.out.println("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        System.out.println("Found User: " + appUser);

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.userRoleRepository.getRoleNames(appUser.getUserId());

        List<GrantedAuthority> grantList = new ArrayList<>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        UserDetails userDetails = new User(appUser.getUserName(), //
                appUser.getEncrytedPassword(), grantList);

        return userDetails;
    }

    public UserDao save(UserDao userDao) {
        System.out.println("22222222222 + userName: " + userDao.getUserName() + ", password: " + userDao.getPassword());
        AppUser appUser = new AppUser();
        appUser.setUserName(userDao.getUserName());
        appUser.setEncrytedPassword(EncryptedPasswordUtils.encryptePassword(userDao.getPassword()));
        appUser.setEnabled(true);
        AppUser savedUser = userDetailsRepository.save(appUser);
        return convertToDTO(savedUser);
    }

    private UserDao convertToDTO(AppUser appUser) {
        UserDao user = modelMapper.map(appUser, UserDao.class);
        user.setPassword(null);
        return user;
    }
}
