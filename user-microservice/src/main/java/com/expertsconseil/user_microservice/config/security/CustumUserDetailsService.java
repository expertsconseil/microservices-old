package com.expertsconseil.user_microservice.config.security;



import com.expertsconseil.user_microservice.models.db.User;
import com.expertsconseil.user_microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class CustumUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return  userRepository.findByUsername(username)
                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found for username "+username)))
                .map(User::toCustumUserDetails);
    }
}
