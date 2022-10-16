package by.tovpenets.secure.api.service;

import by.tovpenets.secure.api.entity.Role;
import by.tovpenets.secure.api.entity.User;
import by.tovpenets.secure.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User register (User user){
        Role role = roleService.findByName("ROLE_AUTHOR");
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> getUser(Integer id){
        return userRepository.findById(id);
    }


    public User findByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public Optional<User> findByLoginAndPassword(String login, String password){
        User user = userRepository.findByLogin(login);
        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
