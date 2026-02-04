package com.revworkforce.dao;

import com.revworkforce.model.Announcements;

import java.util.List;

public interface IAnnouncementsDao {

    boolean createAnnouncement(Announcements announcements);

    List<Announcements> getAllAnnouncements();

    List<Announcements> getAnnouncementsByUser(int userId);

    boolean updateAnnouncement(Announcements announcements);

    boolean deleteAnnouncement(int announcementId);

    List<Announcements> getAnnouncementsByRole(String role);
}

