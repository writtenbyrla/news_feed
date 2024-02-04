package com.example.news_feed.security;

import com.example.news_feed.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(UserDetailsImpl::from)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found" + email));

//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if("ADMIN".equals(user.getRole())){
//            authorities.add(new SimpleGrantedAuthority(UserRoleEnum.ADMIN.getAuthority()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(UserRoleEnum.USER.getAuthority()));
//        }

//        return new UserDetailsImpl(user);
    }

}
