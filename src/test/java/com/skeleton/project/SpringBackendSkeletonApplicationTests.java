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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collector;

@RunWith(SpringRunner.class)
@Profile("test")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

		User admin = new User();
		admin.setId("3l6FvM305C");
		Set<User> admins = new HashSet<>(Arrays.asList(admin));

		Set<String> lockIds = new HashSet<>();
		lockIds.add("4703");
		lockIds.add("4602");

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

		Lock lock2 = new Lock();
		lock.setLockId("4602");

		// Owner key relationship does live in group land
//		KeyRelationship ownerKr = new KeyRelationship();
//		ownerKr.setId("ownerKr");
//		ownerKr.setKey(lock);
//		ownerKr.setUser(owner);
//
//		KeyRelationship ownerKr2 = new KeyRelationship();
//		ownerKr.setId("ownerKr");
//		ownerKr.setKey(lock2);
//		ownerKr.setUser(owner);

		KeyRelationship userKr = new KeyRelationship();
		userKr.setId("id1");
		userKr.setKey(lock);
		userKr.setUser(user);

		KeyRelationship userKr2 = new KeyRelationship();
		userKr2.setId("id12");
		userKr2.setKey(lock2);
		userKr2.setUser(user);

		KeyRelationship user2Kr = new KeyRelationship();
		user2Kr.setId("id2");
		user2Kr.setKey(lock);
		user2Kr.setUser(user2);

		KeyRelationship user2Kr2 = new KeyRelationship();
		user2Kr2.setId("id22");
		user2Kr2.setKey(lock2);
		user2Kr2.setUser(user2);

		KeyRelationship adminKr = new KeyRelationship();
		adminKr.setId("krAdmin");
		adminKr.setKey(lock);
		adminKr.setUser(admin);

		KeyRelationship adminKr2 = new KeyRelationship();
		adminKr2.setId("krAdmin2");
		adminKr2.setKey(lock2);
		adminKr2.setUser(admin);

		Set<KeyRelationship> keyRelationships = new HashSet<>();
		keyRelationships.add(userKr);
		keyRelationships.add(userKr2);
		keyRelationships.add(user2Kr);
		keyRelationships.add(user2Kr2);
		keyRelationships.add(adminKr);
		keyRelationships.add(adminKr2);

		UserGroup userGroupToAdd = new UserGroup();
		userGroupToAdd.setName("testGroupies");
		userGroupToAdd.setOwner(owner);
		userGroupToAdd.setCanUnlockUntil(canUsersUnlockUntil);
		userGroupToAdd.setCanRemoteUnlock(canUsersRemoteUnlock);
		userGroupToAdd.setLockIds(lockIds);
		userGroupToAdd.setUsers(users);
		userGroupToAdd.setAdmins(admins);
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
		Assert.assertEquals(1, group.getAdmins().size());
		Assert.assertEquals(6, group.getKeyRelationships().size());

		Set<User> admins = group.getAdmins();

		User admin = new User();
		admin.setId("3l6FvM305C");

		Assert.assertTrue(admins.contains(admin));
	}

	@Test
	public void addUserToGroup() {
		User user = new User();
		user.setFirstName("New");
		user.setLastName("Human");
		user.setId("useridz1");

//		User admin = new User();
//		admin.setId("3l6FvM305C");

		User owner = new User();
		owner.setId("CZn2XuFJf3");

		Lock lock = new Lock();
		lock.setLockId("4703");

		Lock lock2 = new Lock();
		lock.setLockId("4602");

//		KeyRelationship adminKr = new KeyRelationship();
//		adminKr.setId("krAdmin");
//		adminKr.setKey(lock);
//		adminKr.setUser(admin);

		KeyRelationship userKr = new KeyRelationship();
		userKr.setId("userKr");
		userKr.setKey(lock);
		userKr.setUser(user);

		KeyRelationship userKr2 = new KeyRelationship();
		userKr2.setId("userKr2");
		userKr2.setKey(lock2);
		userKr2.setUser(user);

		// create request
		UserGroupRequest request = new UserGroupRequest();
		request.setRequestingUser(owner);
		request.setGroupId(testGroupId);
//		request.setKeyRelationships(new HashSet<>(Arrays.asList(adminKr, userKr)));
		request.setKeyRelationships(new HashSet<>(Arrays.asList(userKr, userKr2)));
//		request.setTargetUsers(Arrays.asList(user, admin));
		request.setTargetUsers(Arrays.asList(user));

		UserGroup grabbed = userGroupService.getUserGroup(testGroupId);
		Assert.assertEquals(2, grabbed.getUsers().size());
		Assert.assertEquals(1, grabbed.getAdmins().size());
		Assert.assertEquals(6, grabbed.getKeyRelationships().size());

		UserGroup res = coreEngine.addUsersToGroup(request);
//		UserGroup res = userGroupService.addUsers(testGroupId, Arrays.asList(user, admin));
//		Assert.assertEquals(4, res.getUsers().size());
		Assert.assertEquals(3, res.getUsers().size());
		Assert.assertEquals(1, res.getAdmins().size());
		Assert.assertEquals(8, res.getKeyRelationships().size());

		grabbed = userGroupService.getUserGroup(testGroupId);
//		Assert.assertEquals(4, grabbed.getUsers().size());
		Assert.assertEquals(3, grabbed.getUsers().size());
		Assert.assertEquals(1, grabbed.getAdmins().size());
		Assert.assertEquals(8, grabbed.getKeyRelationships().size());

//		request.setTargetAdmins(new HashSet<>(Arrays.asList(user, admin)));
		request.setTargetAdmins(new HashSet<>(Arrays.asList(user)));
		request.setTargetUsers(Collections.emptyList());

		res = coreEngine.addAdminsToGroup(request);
//		UserGroup res = userGroupService.addUsers(testGroupId, Arrays.asList(user, admin));
		Assert.assertEquals(2, res.getUsers().size());
		Assert.assertEquals(2, res.getAdmins().size());
		Assert.assertEquals(8, res.getKeyRelationships().size());

		grabbed = userGroupService.getUserGroup(testGroupId);
		Assert.assertEquals(2, grabbed.getUsers().size());
		Assert.assertEquals(2, grabbed.getAdmins().size());
		Assert.assertEquals(8, grabbed.getKeyRelationships().size());
	}

	@Test
	public void aa_deleteLock() {
		UserGroupRequest request = new UserGroupRequest();
		request.setGroupId(testGroupId);
		request.setTargetLockIds(Arrays.asList("4602"));

		User owner = new User(); //TODO MAKE A GLOBAL VARIABLE
		owner.setId("CZn2XuFJf3");

		request.setRequestingUser(owner);

		UserGroup group = coreEngine.removeLocksFromGroup(request);
		Assert.assertEquals(3, group.getKeyRelationships().size());

		UserGroup grabbed = userGroupService.getUserGroup(testGroupId);
		Assert.assertEquals(2, grabbed.getUsers().size());
		Assert.assertEquals(1, grabbed.getLockIds().size());
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
