package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static User setupUser(UserService userService) {
        User user1 = userService.save(new User("신사임당", "신사임당@이메일.com", "50000"));
        User user2 = userService.save(new User("세종대왕", "세종대왕@이메일.com", "10000"));
        User user3 = userService.save(new User("율곡이이", "율곡이이@이메일.com", "5000"));
        User user4 = userService.save(new User("퇴계이황", "퇴계이황@이메일.com", "1000"));

        return user2;
    }

    public static Channel setupChannel(ChannelService channelService) {
        Channel channel1 = channelService.save(new Channel("디자이너 채널", "코드잇 디자이너 학생들을 위한 채널 🖼️"));
        Channel channel2 = channelService.save(new Channel("프론트엔드 채널", "코드잇 프론트엔드 학생들을 위한 채널 🍕"));
        Channel channel3 = channelService.save(new Channel("백엔드 채널", "코드잇 백엔드 학생들을 위한 채널 🍔"));

        return channel2;
    }

    public static Message setupMessage(MessageService messageService, UUID userId, UUID channelId) {
        Message message1 = messageService.save(new Message("메세지 입니다. 111", userId, channelId));
        Message message2 = messageService.save(new Message("메세지 입니다. 111", userId, channelId));
        Message message3 = messageService.save(new Message("메세지 입니다. 111", userId, channelId));

        return message1;
    }


    public static void main(String[] args) {
        UserRepository userRepository = JCFUserRepository.getInstance();
        UserService userService = new BasicUserService(userRepository);

        ChannelRepository channelRepository = JCFChannelRepository.getInstance();
        ChannelService channelService = new BasicChannelService(channelRepository);

        MessageRepository messageRepository = JCFMessageRepository.getInstance();
        MessageService messageService = new BasicMessageService(messageRepository);

        User u = setupUser(userService);
        Channel c = setupChannel(channelService);
        Message m = setupMessage(messageService, u.getId(), c.getId());

        /// /////////////////////////////////////////////

        // 유저 테스트 start
        List<User> users = userService.findAll();
        System.out.println("유저 전체 조회: " + users.size() + "명");

        User user = userService.findById(users.get(0).getId());
        System.out.println("유저 단일 조회: " + user.getName());

        user.update("세종대왕 수정", null, null);
        User updatedUser = userService.modify(user);
        System.out.println("수정된 유저: " + updatedUser);
        System.out.println("수정된 유저 조회: " + userService.findById(updatedUser.getId()));

        User deletedUser = userService.deleteById(users.get(0).getId());
        System.out.println("삭제된 유저: " + deletedUser);
        System.out.println("삭제된 유저 조회: " + userService.findById(deletedUser.getId()));


        /// /////////////////////////////////////////////

        // 채널 테스트 start
        List<Channel> channels = channelService.findAll();
        System.out.println("채널 전체 조회: " + channels.size() + "개");

        Channel channel = channelService.findById(channels.get(0).getId());
        System.out.println("채널 단일 조회: " + channel.getName());

        channel.update("디자이너 채널 수정", null);
        Channel updatedchannel = channelService.modify(channel);
        System.out.println("수정된 채널: " + updatedchannel);
        System.out.println("수정된 채널 조회: " + channelService.findById(updatedchannel.getId()));

        Channel deletedChannel = channelService.deleteById(channels.get(0).getId());
        System.out.println("삭제된 유저: " + deletedChannel);
        System.out.println("삭제된 유저 조회: " + channelService.findById(deletedChannel.getId()));

        /// /////////////////////////////////////////////

        // 메세지
        List<Message> messages = messageService.findAll();
        System.out.println("채널 메세지 조회: " + messages.size() + "개");

        Message message = messageService.findById(messages.get(0).getId());
        System.out.println("채널 메세지 조회: " + message.getContent());

        message.update("메세지 입니다. 111 수정수정");
        Message updatedMessage = messageService.modify(message);
        System.out.println("수정된 메세지: " + updatedMessage);
        System.out.println("수정된 메세지 조회: " + messageService.findById(messages.get(0).getId()));

        Message deletedMassage = messageService.deleteById(message.getId());
        System.out.println("삭제된 메세지: " + deletedMassage);
        System.out.println("삭제된 메세지 조회: " + messageService.findById(messages.get(0).getId()));
    }
}
