package com.karankumar.bookproject.security;

import com.karankumar.bookproject.backend.entity.User;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsPasswordService implements UserDetailsPasswordService {
    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    public DatabaseUserDetailsPasswordService(UserRepository userRepository,
                                              UserDetailsMapper userDetailsMapper) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                                  .orElseThrow(() -> new UsernameNotFoundException(
                                          "User with the username " + userDetails.getUsername() + " was not found."));
        user.setPassword(newPassword);
        return userDetailsMapper.toUserDetails(user);
    }
}
