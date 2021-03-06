package casuals.filthy.playmaker.data.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupBean extends DataBean {

    protected long id;
    protected List<GroupUserBean> users;
    protected List<GroupEventData> events = new ArrayList<GroupEventData>();
    protected List<String> eventTypes = new ArrayList<String>();
    protected List<PollBean> polls;
    protected List<Notification> notifications = new ArrayList<Notification>();

    private static String comparing;

    public GroupBean() {};

    public GroupBean(long id, String name) {
        this.id = id;
        this.name = name;
        users = new ArrayList<GroupUserBean>();
        events = new ArrayList<GroupEventData>();
        eventTypes = new ArrayList<String>();
        polls = new ArrayList<PollBean>();
        notifications = new ArrayList<Notification>();
    }

    public boolean isUserAdmin(String userId) {
        for (GroupUserBean user: users) {
            if (user.id.equals(userId))
                return user.isAdmin();
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

    public PollBean getPoll(long id) {
        for (PollBean poll: polls) {
            if (poll.getId() == id)
                return poll;
        }
        return null;
    }

    public List<GroupEventData> getEventsUpcoming() {
        Collections.sort(events);
        List<GroupEventData> result = new ArrayList<GroupEventData>();
        for (int i = 0; i < events.size(); i++) {
            if (System.currentTimeMillis() <= events.get(i).date + 1000 * 60 * 60 * 6 && !events.get(i).isReported()) {
                result.add(events.get(i));
            }
        }

        return result;
    }

    public List<GroupEventData> getEventsUnreported() {
        Collections.sort(events);
        List<GroupEventData> result = new ArrayList<GroupEventData>();
        for (int i = 0; i < events.size(); i++) {
            if (System.currentTimeMillis() > events.get(i).date + 1000 * 60 * 60 * 6 || events.get(i).isReported()) {
                result.add(events.get(i));
            }
        }

        return result;
    }

    public long getId() {
        return id;
    }

    public List<GroupUserBean> getUsers() {
        return users;
    }

    public List<GroupEventData> getEvents() {
        return events;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public List<PollBean> getPolls() {
        return polls;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<GroupUserBean> getRanksList(String type) {
        List<GroupUserBean> ranked = new ArrayList<GroupUserBean>();
        for (GroupUserBean user: users) {
            if (user.getStats() != null  && user.getStats().get(type) != null && user.getStats().get(type).computeScore() > 0) {
                ranked.add(user);
            }
        }

        comparing = type;
        Collections.sort(ranked, new Comparator<GroupUserBean>() {
            @Override
            public int compare(GroupUserBean lhs, GroupUserBean rhs) {
                return  lhs.getStats().get(comparing).compareTo(rhs.getStats().get(comparing));
            }
        });

        return ranked;
    }

    /*public List<Map<String, String>> getRanks(String type) {

        List<GroupUserBean> ranked = getRanksList(type);

        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        int i = 1;
        for (GroupUserBean user: ranked) {
            Map<String, String> entry = new HashMap<String, String>();
            entry.put(LeaderboardEntries.KEY_NAME, user.getName());
            entry.put(LeaderboardEntries.KEY_POINTS, user.getStats().get(type).computeScore()+"");
            switch (i) {
                case 1:
                    entry.put(LeaderboardEntries.KEY_PLACE, "1st Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.first_place));
                    break;
                case 2:
                    entry.put(LeaderboardEntries.KEY_PLACE, "2nd Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.second_place));
                    break;
                case 3:
                    entry.put(LeaderboardEntries.KEY_PLACE, "3rd Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.third_place));
                    break;
                default:
                    entry.put(LeaderboardEntries.KEY_PLACE, i + "th Place");
                    entry.put(LeaderboardEntries.KEY_ICON,  String.valueOf(R.drawable.std_ribbon));
                    break;
            }

            results.add(entry);
            i++;
        }

        return results;
    }*/

    public List<Map<String, String>> getRecentNotification() {
        int i = 1;
        int index;
        SimpleDateFormat format = new SimpleDateFormat("h:mm a M/dd/yy");
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        while (i <= 20 && (index=notifications.size() - i) >= 0) {

            HashMap<String, String> value = new HashMap<String, String>();
            value.put("MESSAGE", notifications.get(index).getBody());
            value.put("DATE", format.format(new Date(notifications.get(index).getDate())));
            value.put("NAME", "-" + notifications.get(index).getName());
            results.add(value);
            i++;
        }

        return results;
    }

    public static class GroupEventData implements Comparable<GroupEventData>{
        public String name;
        public long date;
        public long eventId;
        protected boolean reported = false;

        public GroupEventData() {};

        public String getName() {
            return name;
        }

        public long getDate() {
            return date;
        }

        public long getEventId() {
            return eventId;
        }

        public boolean isReported() {
            return reported;
        }

        @Override
        public int compareTo(GroupEventData another) {
            return (int) (this.date - another.date);
        }
    }

    public static class Notification {
        public String name;
        public String body;
        public long date;

        public String getName() {
            return name;
        }

        public String getBody() {
            return body;
        }

        public long getDate() {
            return date;
        }
    }
}
