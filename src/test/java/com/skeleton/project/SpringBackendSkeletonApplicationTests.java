package com.skeleton.project;

import com.skeleton.project.domain.Schedule;
import com.skeleton.project.domain.User;
import com.skeleton.project.domain.UserGroup;
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

	@Test
	public void createUserGroup() {

		List<String> adminIds = new ArrayList<>();

		List<String> lockIds = new ArrayList<>();

		Schedule schedule = Schedule.builder().id("GnOHW6uvA8").build();
		Schedule schedule2 = Schedule.builder().id("3nxeEYEJi7").build();
		List<Schedule> schedules = Arrays.asList(schedule, schedule2);

		User user = User.builder().id("pKPes1hdQE").build();
		User user2 = User.builder().id("FgJCRVDiB5").build();
		List<User> users = Arrays.asList(user, user2);

		boolean canUsersRemoteUnlock = true;
		boolean canUsersUnlockUntil = false;

		UserGroup userGroupToAdd = UserGroup.builder()
				.name("TestGroup")
				.canRemoteUnlock(canUsersRemoteUnlock)
				.canUnlockUntil(canUsersUnlockUntil)
				.lockIds(lockIds)
				.users(users)
				.admins(new ArrayList<>())
				.schedule(schedules)
				.build();
		try {
//			UserGroup userGroup = userGroupService.createUserGroup(adminIds, lockIds, schedules, userIds, canUsersRemoteUnlock, canUsersUnlockUntil);
//			UserGroup dbUserGroup = userGroupService.getUserGroup(userGroup.getId());
//			Assert.assertEquals(userGroup, dbUserGroup);

			UserGroup obj = userGroupService.createUserGroup(userGroupToAdd);
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

//	@Test
	public void createUser() {

		User user = User.builder()
				.primaryPhone("+12024047575")
				.primaryEmail("test@test.com")
				.username("username2")
				.firstName("Frank")
				.lastName("Farina")
				.type(0)
				.build();

		try {
			Key key = userService.createUser(user);

			User dbUser = userService.getUser((String)key.getId());

			Assert.assertEquals(user, dbUser);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
