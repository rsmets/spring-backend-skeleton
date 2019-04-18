package com.skeleton.project;

import com.skeleton.project.dto.entity.Schedule;
import com.skeleton.project.dto.entity.User;
import com.skeleton.project.dto.entity.KeyRelationship;
import com.skeleton.project.dto.entity.UserGroup;
import com.skeleton.project.service.IKeyRelationshipService;
import com.skeleton.project.service.IUserGroupService;
import com.skeleton.project.service.IUserService;
import dev.morphia.Key;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@Profile("test")
@SpringBootTest
public class SpringBackendSkeletonApplicationTests {

	@Autowired
	IUserGroupService userGroupService;

	@Autowired
	IUserService userService;

	@Autowired
	IKeyRelationshipService keyRelationshipService;

	@Test
	public void createUserGroup() {

		List<String> adminIds = new ArrayList<>();

		List<String> lockIds = new ArrayList<>();

		Schedule schedule = new Schedule();
		schedule.setId("GnOHW6uvA8");
		Schedule schedule2= new Schedule();
		schedule2.setId("3nxeEYEJi7");
		List<Schedule> schedules = Arrays.asList(schedule, schedule2);

//		User user = User.builder().id("pKPes1hdQE").build();
//		User user2 = User.builder().id("FgJCRVDiB5").build();
		User user = new User();
		user.setId("FgJCRVDiB5");
		User user2 = new User();
		user2.setId("pKPes1hdQE");
		List<User> users = Arrays.asList(user, user2);


		boolean canUsersRemoteUnlock = true;
		boolean canUsersUnlockUntil = false;

//		UserGroup userGroupToAdd = UserGroup.builder()
//				.name("TestGroup")
//				.canRemoteUnlock(canUsersRemoteUnlock)
//				.canUnlockUntil(canUsersUnlockUntil)
//				.lockIds(lockIds)
//				.users(users)
//				.admins(new ArrayList<>())
//				.schedule(schedules)
//				.keyRelationships(Collections.emptyList())
//				.build();
		UserGroup userGroupToAdd = new UserGroup();
		userGroupToAdd.setName("testGroupies");
		userGroupToAdd.setCanUnlockUntil(canUsersUnlockUntil);
		userGroupToAdd.setCanRemoteUnlock(canUsersRemoteUnlock);
		userGroupToAdd.setLockIds(lockIds);
		userGroupToAdd.setUsers(users);
		userGroupToAdd.setSchedule(schedules);
		try {
//			UserGroup userGroup = userGroupService.createUserGroup(adminIds, lockIds, schedules, userIds, canUsersRemoteUnlock, canUsersUnlockUntil);
//			UserGroup dbUserGroup = userGroupService.getUserGroup(userGroup.getId());
//			Assert.assertEquals(userGroup, dbUserGroup);

			UserGroup obj = userGroupService.createUserGroup(userGroupToAdd);
//			String id = obj.getId().toHexString();
//			UserGroup dbUserGroup = userGroupService.getUserGroup(obj.getId());
			UserGroup dbUserGroup = userGroupService.getUserGroup(obj.getId());

			Assert.assertEquals(userGroupToAdd, dbUserGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getUser() {

		User dbUser = userService.getUser("3l6FvM305C");
		Assert.assertEquals(dbUser.getLastName(), "Smets");
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

	@Test
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
