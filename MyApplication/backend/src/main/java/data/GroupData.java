package data;

import com.googlecode.objectify.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 3/30/2015.
 */
@Entity
public class GroupData extends DataObject {

    @Id
    public long id;
    public List<GroupUserData> users;
    public List<GroupEventData> events;

    public GroupData() {};

    public GroupData(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<GroupUserData>();
        events = new ArrayList<GroupEventData>();
    }


    public void addUser(UserData user) {
        users.add(new GroupUserData(user.id, user.name));
    }

    public void addAdmin(UserData user) {
        users.add(new GroupUserData(user.id, user.name, true));
    }

    public void addEvent(EventData event) {
        if (events == null)
            events = new ArrayList<GroupEventData>();
        events.add(new GroupEventData(event.name, event.date, event.id));
    }

    public boolean isUserAdmin(String userId) {
        for (GroupUserData user: users) {
            if (user.id.equals(userId))
                return true;
        }

        return false;
    }

    public GroupEventData getEvent(long id) {
        for (GroupEventData event: events) {
            if (event.eventId == id)
                return event;
        }

        return null;
    }


    public static class GroupUserData {

        public String name;
        public String id;
        public String type;
        public boolean admin;

        public GroupUserData() {};

        public GroupUserData(String userId, String name) {
            id = userId;
            this.name = name;
            admin = false;
        }

        public GroupUserData(String userId, String name, boolean admin) {
            id = userId;
            this.name = name;
            this.admin = admin;
        }



    }

    public static class GroupEventData {
        public String name;
        public long date;
        public long eventId;

        public GroupEventData() {};

        public GroupEventData(String name, long date, long id) {
            this.name = name;
            this.date = date;
            this.eventId = id;
        }
    }
}