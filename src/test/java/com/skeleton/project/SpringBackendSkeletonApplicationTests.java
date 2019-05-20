package com.skeleton.project;

import com.skeleton.project.core.ICoreEngine;
import com.skeleton.project.dto.api.UserGroupRequest;
import com.skeleton.project.dto.entity.*;
import com.skeleton.project.service.IKeyRelationshipService;
import com.skeleton.project.service.IUserGroupService;
import com.skeleton.project.service.IUserService;
import dev.morphia.Key;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@Profile("test")
@SpringBootTest
public class SpringBackendSkeletonApplicationTests {

	@Autowired
	ICoreEngine coreEngine;

	@Autowired
	IUserGroupService userGroupService;

	@Autowired
	IUserService userService;

	@Autowired
	IKeyRelationshipService keyRelationshipService;

	String testGroupId;
	/**
	 * TODO make this the first step in the testing process so that the id can be used for the testing through out
	 */
	@Before
	public void createUserGroup() {

		if (testGroupId != null) return;

		List<String> adminIds = new ArrayList<>();

		User owner = new User();
		owner.setId("CZn2XuFJf3");

		Set<String> lockIds = new HashSet<>();
		lockIds.add("4703");

		Schedule schedule = new Schedule();
		schedule.setId("GnOHW6uvA8");
		Schedule schedule2= new Schedule();
		schedule2.setId("3nxeEYEJi7");
		List<Schedule> schedules = Arrays.asList(schedule, schedule2);

		User user = new User();
		user.setId("FgJCRVDiB5");
		User user2 = new User();
		user2.setId("pKPes1hdQE");
		Set<User> users = new HashSet<>(Arrays.asList(user, user2));

		boolean canUsersRemoteUnlock = true;
		boolean canUsersUnlockUntil = false;

		Lock lock = new Lock();
		lock.setLockId("4703");

		KeyRelationship ownerKr = new KeyRelationship();
		ownerKr.setId("id1");
		ownerKr.setKey(lock);
		ownerKr.setUser(owner);

		KeyRelationship userKr = new KeyRelationship();
		userKr.setId("id2");
		userKr.setKey(lock);
		userKr.setUser(user);

		KeyRelationship user2Kr = new KeyRelationship();
		user2Kr.setId("id3");
		user2Kr.setKey(lock);
		user2Kr.setUser(user2);

		Set<KeyRelationship> keyRelationships = new HashSet<>();
		keyRelationships.add(ownerKr);
		keyRelationships.add(userKr);
		keyRelationships.add(user2Kr);

		UserGroup userGroupToAdd = new UserGroup();
		userGroupToAdd.setName("testGroupies");
		userGroupToAdd.setOwner(owner);
		userGroupToAdd.setCanUnlockUntil(canUsersUnlockUntil);
		userGroupToAdd.setCanRemoteUnlock(canUsersRemoteUnlock);
		userGroupToAdd.setLockIds(lockIds);
		userGroupToAdd.setUsers(users);
		userGroupToAdd.setSchedule(schedules);
		userGroupToAdd.setKeyRelationships(keyRelationships);

		try {
			UserGroup obj = userGroupService.createUserGroup(userGroupToAdd);
			UserGroup dbUserGroup = userGroupService.getUserGroup(obj.getId());

			Assert.assertEquals(userGroupToAdd, dbUserGroup);

			testGroupId = obj.getId();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getUserGroup() {
		UserGroup group = userGroupService.getUserGroup(testGroupId);
		Assert.assertEquals("testGroupies", group.getName());
	}

	@Test
	public void addUserToGroup() {
		User user = new User();
		user.setFirstName("New");
		user.setLastName("Human");

		UserGroup res = userGroupService.addUsers(testGroupId, Arrays.asList(user));
		Assert.assertEquals(3, res.getUsers().size());

		UserGroup grabbed = userGroupService.getUserGroup(testGroupId);
		Assert.assertEquals(3, grabbed.getUsers().size());
	}

	@Test
	public void deleteGroup() throws Exception {

		UserGroup grabbed = userGroupService.getUserGroup(testGroupId);

		User user = new User();
		user.setId("CZn2XuFJf3");

		UserGroupRequest request = new UserGroupRequest();
		request.setGroupId(testGroupId);
		request.setRequestingUser(user);
		Set<KeyRelationship> res = coreEngine.deleteUserGroup(request);
		Assert.assertEquals(grabbed.getKeyRelationships().size(), res.size());
	}

	@Test
	public void getUser() {

		User dbUser = userService.getUser("3l6FvM305C");
		Assert.assertEquals("Smets", dbUser.getLastName());
	}

	@Test
	public void getUserByPhoneNumber() {

		User dbUser = userService.getUserByPhone("+14044327575");
		Assert.assertEquals(dbUser.getLastName(), "Smets");
	}

	@Test
	public void getUserByEmail() {

		User dbUser = userService.getUserByEmail("ray.smets@nexkey.com");
		Assert.assertEquals(dbUser.getLastName(), "Smets");
	}

//	@Test
	public void getKeyRelationship() {

		KeyRelationship kr = keyRelationshipService.getKeyRelationship("3dy7V2SSoN");
		Assert.assertEquals(kr.getId(), "3dy7V2SSoN");
	}

	// Can not test create and grab due to deciding against handling parse land collection creation in this external service.
//	@Test
	public void createUser() {

		User user = new User();
		user.setPrimaryEmail("test@test.com");
		user.setPrimaryPhone("+12024047575");
		user.setUsername("username5");
		user.setFirstName("Frank");
		user.setLastName("Farina");
		user.setType(0);

		try {
			Key key = userService.createUser(user);

			User dbUser = userService.getUser((String)key.getId());

			Assert.assertEquals(user, dbUser);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
