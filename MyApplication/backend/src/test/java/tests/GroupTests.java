package tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

import data.GroupData;
import data.UserData;

/**
 * Created by Chris on 4/17/2015.
 */
public class GroupTests {

    UserData user1;
    UserData user2;
    UserData user3;
    UserData user4;
    UserData user5;
    UserData user6;
    UserData user7;
    UserData user8;

    @Before
    public void makeUsers() {
        Random rand = new Random();
        user1 = Utils.addUser("groups1" + rand.nextInt(100000), "leedle", "loodle");
        user2 = Utils.addUser("groups2" + rand.nextInt(100000), "leedle", "loodle");
        user3 = Utils.addUser("groups3" + rand.nextInt(100000), "leedle", "loodle");
        user4 = Utils.addUser("groups4" + rand.nextInt(100000), "leedle", "loodle");
        user5 = Utils.addUser("groups5" + rand.nextInt(100000), "leedle", "loodle");
        user6 = Utils.addUser("groups6" + rand.nextInt(100000), "leedle", "loodle");
        user7 = Utils.addUser("groups7" + rand.nextInt(100000), "leedle", "loodle");
        user8 = Utils.addUser("groups8" + rand.nextInt(100000), "leedle", "loodle");
    }

    @Test
    public void groupsCreateGroup() {
        GroupData group = Utils.addGroup(user1.getId(), "group1");
        assertNotNull(group);
        assertEquals("group1", group.getName());
        assertTrue(group.users.size() == 1);
        assertEquals(group.getUsers().get(0).getName(), user1.getName());
        assertTrue(group.getUsers().get(0).isAdmin());
    }

    @Test
    public void groupsGetGroup() {
        GroupData group1 = Utils.addGroup(user1.getId(), "group2");
        GroupData group2 = Utils.getGroup(group1.getId());

        assertEquals(group1.getName(), group2.getName());
        assertEquals(group1.getId(), group2.getId());
        assertEquals(group1.getUsers().size(), group2.getUsers().size());
    }

    @Test
    public void groupsJoinGroup() {
        GroupData group = Utils.addGroup(user5.getId(), "joiningGroup");
        UserData userJoined = Utils.joinGroup(user6.getId(), group.getId());
        assertNotNull(userJoined);
        System.out.println(userJoined.getGroups().size());
        assertEquals(userJoined.getGroups().size(), 1);
        assertEquals(user6.getId(), userJoined.getId());
        // need to update the group data
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 2);
        assertEquals(group.getUsers().get(1).getId(), userJoined.getId());

        // test more
        Utils.joinGroup(user1.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 3);
        Utils.joinGroup(user2.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 4);
        Utils.joinGroup(user3.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 5);
        // test a duplicate
        Utils.joinGroup(user5.getId(), group.getId());
        group = Utils.getGroup(group.getId());
        assertEquals(group.getUsers().size(), 5);

    }


}