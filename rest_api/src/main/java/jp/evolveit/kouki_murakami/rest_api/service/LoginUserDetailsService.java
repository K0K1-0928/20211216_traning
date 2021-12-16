package jp.evolveit.kouki_murakami.rest_api.service;

import jp.evolveit.kouki_murakami.rest_api.model.LoginUserDetails;
import jp.evolveit.kouki_murakami.rest_api.model.User;
import jp.evolveit.kouki_murakami.rest_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public LoginUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("該当するユーザーが存在しません。");
        return new LoginUserDetails(user);
    }
}
