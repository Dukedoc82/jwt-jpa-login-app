package com.dyukov.taxi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.model.TpUserDetails;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserRoleRepository;
import com.dyukov.taxi.service.IUserDetailsService;
import com.dyukov.taxi.utils.EncryptedPasswordUtils;
import com.dyukov.taxi.utils.IValidationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements IUserDetailsService, UserDetailsService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IValidationUtils validationUtils;

    @Autowired
    private IActivationTokenRepository activationTokenRepository;

    @Override
    public TpUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TpUser tpUser = userDetailsRepository.findUserAccount(username);

        if (tpUser == null) {
            logger.warn("User not found! " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        Collection roleNames = this.userRoleRepository.getRoleNames(tpUser.getUserId());


        List<GrantedAuthority> grantList = new ArrayList<>();
        logger.info("Roles length: " + roleNames.size());
        if (roleNames != null) {
            for (Object role : roleNames) {
                String roleName = (String) role;
                logger.info("Found role name: " + roleName);
                GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
                grantList.add(authority);
            }
        }

        return new TpUserDetails(convertToDTO(tpUser), tpUser.getUserName(), //
                tpUser.getEncrytedPassword(), grantList);
    }

    public UserDao save(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        tpUser.setEnabled(false);
        TpUser savedUser = userDetailsRepository.saveUser(tpUser);
        return convertToDTO(savedUser);
    }

    public UserDao saveAdmin(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        TpUser savedUser = userDetailsRepository.saveAdmin(tpUser);
        return convertToDTO(savedUser);
    }

    public UserDao saveDriver(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        tpUser.setEnabled(false);
        TpUser savedUser = userDetailsRepository.saveDriver(tpUser);
        return convertToDTO(savedUser);
    }

    public UserDao activateUser(String activationToken) {
        return null;
    }

    public Collection findAll() {
        return convertToDao(userDetailsRepository.findAll());
    }

    @Override
    public Collection findDrivers() {
        return convertToDao(userDetailsRepository.findDrivers());
    }

    @Override
    public Collection findAdmins() {
        return convertToDao(userDetailsRepository.findAdmins());
    }

    @Override
    public Collection findUsers() {
        return convertToDao(userDetailsRepository.findUsers());
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

    private Collection<UserDao> convertToDao(Collection<TpUser> entityUsers) {
        return entityUsers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
