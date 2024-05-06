package com.auth.api.repository;

import com.auth.api.exceptions.UserNotFoundExceptionToDeleteException;
import com.auth.api.exceptions.UserNotFoundToUpdateException;
import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final List<User> dummyUserData = new ArrayList<>() {{
        add(new User(UUID.randomUUID(), "tim@eu.com", "passw"));
        add(new User(UUID.randomUUID(), "mike@eu.com", "mikepassword"));
        add(new User(UUID.randomUUID(), "paul@eu.com", "paulpassword"));
        add(new User(UUID.randomUUID(), "cristina@eu.com", "cristinapassword"));
        add(new User(UUID.randomUUID(), "simida@eu.com", "simidapassword"));
        add(new User(UUID.randomUUID(), "alin@eu.com", "alinpassword"));
        add(new User(UUID.randomUUID(), "flavi@eu.com", "flavipassword"));
        add(new User(UUID.randomUUID(), "andrei@eu.com", "andreipassword"));
        add(new User(UUID.randomUUID(), "mihai@eu.com", "mihaipassword"));
        add(new User(UUID.randomUUID(), "alina@eu.com", "alinapassword"));
        add(new User(UUID.randomUUID(), "darius@eu.com", "dariuspassword"));
    }};

    public List<User> getAllUsers() {
        return dummyUserData;
    }

    public void updateUser(User updatedUser) throws UserNotFoundToUpdateException {
        boolean found = false;
        for (int i = 0; i < dummyUserData.size(); i++) {
            User user = dummyUserData.get(i);
            if (user.getId() == updatedUser.getId()) {
                dummyUserData.set(i, updatedUser);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UserNotFoundToUpdateException(updatedUser.getId());
        }
    }

    public void deleteUserById(UUID id) throws UserNotFoundExceptionToDeleteException {
        Iterator<User> iterator = dummyUserData.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == id) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new UserNotFoundExceptionToDeleteException(id);
        }
    }

    public User findUserByID(UUID id) {
        Optional<User> foundUser = dummyUserData.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        return foundUser.orElse(null);
    }

    public User findByEmail(String email) {
        Optional<User> foundUser = dummyUserData.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
        return foundUser.orElse(null);
    }

    public User addUser(User newUser) throws DuplicateUserException {
        boolean isDuplicate = dummyUserData.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(newUser.getEmail()));

        if (isDuplicate) {
            throw new DuplicateUserException(newUser.getEmail());
        } else {
            dummyUserData.add(newUser);
        }
        return newUser;
    }

}
