package com.dyukov.taxi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.dao.UserRolesDao;
import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.model.TpUserDetails;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserRoleRepository;
import com.dyukov.taxi.service.IActivationUserService;
import com.dyukov.taxi.service.IUserDetailsService;
import com.dyukov.taxi.utils.EncryptedPasswordUtils;
import com.dyukov.taxi.utils.IValidationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
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
    private IActivationUserService activationUserService;

    @Override
    public TpUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getTpUserDetailsFromTpUser(userDetailsRepository.findUserAccount(username));
    }

    @Override
    public TpUserDetails findUser(Long id) {
        return getTpUserDetailsFromTpUser(userDetailsRepository.findUserAccount(id));
    }

    public UserDao save(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        tpUser.setEnabled(false);
        UserDao savedUser = convertToDTO(userDetailsRepository.saveUser(tpUser));
        activationUserService.generateActivationToken(savedUser);
        return savedUser;
    }

    public UserDao saveAdmin(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        tpUser.setEnabled(false);
        UserDao savedUser = convertToDTO(userDetailsRepository.saveAdmin(tpUser));
        activationUserService.generateActivationToken(savedUser);
        return savedUser;
    }

    public UserDao saveDriver(RegistrationData registrationData) {
        validationUtils.validateUser(registrationData);
        TpUser tpUser = convertFromDto(registrationData);
        tpUser.setEnabled(false);
        UserDao savedUser = convertToDTO(userDetailsRepository.saveDriver(tpUser));
        activationUserService.generateActivationToken(savedUser);
        return savedUser;
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

    @Override
    public UserRolesDao getUserRoles(Long userIdFromToken) {
        UserRolesDao roles = new UserRolesDao();
        Collection roleNames = userRoleRepository.getRoleNames(userIdFromToken);
        roles.setAdmin(roleNames.contains("ROLE_ADMIN"));
        roles.setDriver(roleNames.contains("ROLE_DRIVER"));
        return roles;
    }

    private UserDao convertToDTO(TpUser tpUser) {
        return modelMapper.map(tpUser, UserDao.class);
    }

    private TpUser convertFromDto(RegistrationData registrationData) {
        TpUser tpUser = modelMapper.map(registrationData, TpUser.class);
        tpUser.setEncrytedPassword(EncryptedPasswordUtils.encryptePassword(registrationData.getPassword()));
        return tpUser;
    }

    private Collection<UserDao> convertToDao(Collection<TpUser> entityUsers) {
        return entityUsers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    private TpUserDetails getTpUserDetailsFromTpUser(TpUser tpUser) {
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

        return new TpUserDetails(tpUser, tpUser.getUserName(), //
                tpUser.getEncrytedPassword(), grantList);
    }
}
