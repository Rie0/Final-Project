package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.noob.Model.Organizer;
import org.twspring.noob.Repository.OrganizerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;

    public List<Organizer> getOrganizers() {
        return organizerRepository.findAll();
    }

    public Organizer getOrganizerById(Integer id) {
        return organizerRepository.findOrganizerById(id);
    }

    public Organizer saveOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    public void updateOrganizer(Integer organizerId, Organizer updatedOrganizer) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer != null) {
            organizer.setName(updatedOrganizer.getName());
            organizer.setContactInfo(updatedOrganizer.getContactInfo());
            organizer.setOrganizationName(updatedOrganizer.getOrganizationName());
            organizerRepository.save(organizer);
        }
    }

    public void deleteOrganizer(Integer organizerId) {
        organizerRepository.deleteById(organizerId);
    }
}
