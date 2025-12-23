package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        User user = userService.create(new UserCreateRequest("woody", "woody@codeit.com", "woody1234"), null);
        return user;
    }

    static Channel setupChannel(ChannelService channelService, UserService userService ) {
        Channel channel = channelService.create(new PublicChannelCreateRequest("공지1", "공지 채널입니다."));
        channelService.create(new PublicChannelCreateRequest("공지2", "공지 채널입니다."));
        channelService.create(new PublicChannelCreateRequest("공지3", "공지 채널입니다."));
        channelService.create(new PublicChannelCreateRequest("공지4", "공지 채널입니다."));
        channelService.create(new PublicChannelCreateRequest("공지5", "공지 채널입니다."));

        // 유저 생성
        User user1 = userService.create(new UserCreateRequest("woody1", "woody1@codeit.com", "woody1234"), null);
        User user2 = userService.create(new UserCreateRequest("woody2", "woody2@codeit.com", "woody1234"), null);

        // 비공개방 생성
        List<UUID> list = new ArrayList<>(List.of(user1.getId(), user2.getId()));
        channelService.create(new PrivateChannelCreateRequest(list));

        // 유저1이 입장 가능한 모든 방 조회
        channelService.findAllByUserId(user1.getId()).forEach(System.out::println);

        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageCreateRequest request = new MessageCreateRequest("안녕하세요.", channel.getId(), author.getId());
        Message message = messageService.create(request, new ArrayList<>());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        // 서비스 초기화
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, userService);

        // 테스트

    }
}
