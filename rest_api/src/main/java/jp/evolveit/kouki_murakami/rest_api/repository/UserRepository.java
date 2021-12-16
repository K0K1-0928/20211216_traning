package jp.evolveit.kouki_murakami.rest_api.repository;

import jp.evolveit.kouki_murakami.rest_api.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> users;

    public UserRepository() {
        users = new HashMap<>();
        // デモ用のユーザー(username: test, password: test1234)
        User user = new User("test",
                "6a36963092bace2ec288275f67c1f114447fb959980fd2bbd5b78c1fa9b70666b93cd095d533327d");
        users.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {
        return users.containsKey(username) ? users.get(username) : null;
    }
}
