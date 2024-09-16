package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.OrganizerDTO;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Model.User;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.OrganizerRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final AuthRepository userRepository;

    // Get all organizers
    public List<Organizer> getOrganizers() {
        return organizerRepository.findAll();
    }

    // Register a new organizer
    public void registerOrganizer(OrganizerDTO organizerDTO) {
        // Create and save the User entity
        User user = new User();
        String hash= new BCryptPasswordEncoder().encode(organizerDTO.getPassword());

        user.setUsername(organizerDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(organizerDTO.getEmail());
        user.setPhoneNumber(organizerDTO.getPhoneNumber());
        user.setBirthday(organizerDTO.getBirthday());
        user.setAge(Period.between(user.getBirthday(), LocalDate.now()).getYears());
        if (user.getAge()<18){
            throw new ApiException("Organizers under the age of 18 are prohibited from registering in our system");
        }
        user.setRole("ORGANIZER");
        userRepository.save(user);

        // Create and save the Organizer entity
        Organizer organizer = new Organizer();
        organizer.setName(organizerDTO.getName());
        organizer.setContactInfo(organizerDTO.getContactInfo());
        organizer.setOrganizationName(organizerDTO.getOrganizationName());
        organizer.setUser(user);
        organizerRepository.save(organizer);
    }

    // Update an existing organizer
    public void updateOrganizer(Integer organizerId, OrganizerDTO organizerDTO) {
        Organizer existingOrganizer = organizerRepository.findOrganizerById(organizerId);
        if (existingOrganizer == null) {
            throw new ApiException("Organizer not found");
        }

        User existingUser = existingOrganizer.getUser();
        existingUser.setUsername(organizerDTO.getUsername());
        String hash= new BCryptPasswordEncoder().encode(organizerDTO.getPassword());

        existingUser.setPassword(hash);
        existingUser.setEmail(organizerDTO.getEmail());
        existingUser.setPhoneNumber(organizerDTO.getPhoneNumber());
        userRepository.save(existingUser);

        existingOrganizer.setName(organizerDTO.getName());
        existingOrganizer.setContactInfo(organizerDTO.getContactInfo());
        existingOrganizer.setOrganizationName(organizerDTO.getOrganizationName());
        organizerRepository.save(existingOrganizer);
    }

    // Delete an organizer
    public void deleteOrganizer(Integer organizerId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        userRepository.deleteById(organizer.getUser().getId());
        organizerRepository.deleteById(organizerId);
    }
}