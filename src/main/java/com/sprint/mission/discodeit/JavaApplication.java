package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static void userCRUDTest(UserService userService) {
        System.out.println("\n유저 테스트");
        User user1 = new User("홍길동", "홍길동 이메일.com", "1234");
        User user2 = new User("이순신", "이순신 이메일.com", "5678");

        // 생성
        UserDTO user1DTO = userService.save(user1);
        UserDTO user2DTO = userService.save(user2);
        System.out.println("유저 생성: " + user1DTO);
        System.out.println("유저 생성: " + user2DTO);

        // 조회 [단건]
        UserDTO find1 = userService.findById(user1.getId());
        UserDTO find2 = userService.findById(UUID.randomUUID()); // 없는 ID
        System.out.println("유저 조회 [단건]: " + find1);
        System.out.println("유저 조회 [단건] - 없는 ID로 조회: " + find2);

        // 조회 [다건]
        List<UserDTO> findAll = userService.findAll();
        System.out.println("유저 조회 [다건] - 모든 사용자 조회: " + findAll);

        // 수정
        user1.update("이름수정", "이메일수정", "비번수정");
        UserDTO modifiedUser = userService.modify(user1);
        System.out.println("수정된 유저 = " + modifiedUser);
        UserDTO modifyChk = userService.findById(user1.getId());
        System.out.println("수정확인을 위한 조회 = " + modifyChk);

        // 삭제
        UserDTO deletedUser = userService.deleteById(user1.getId());
        System.out.println("삭제된 유저 = " + deletedUser);
        UserDTO deleteChk = userService.findById(user1.getId());
        System.out.println("삭제확인을 위한 조회 = " + deleteChk);
    }

    public static void channelCRUDTest(ChannelService channelService) {
        System.out.println("\n채널 테스트");

        Channel channel1 = new Channel("채널 1번", "디자이너 디코방");
        Channel channel2 = new Channel("채널 2번", "프론트엔드 디코방");
        Channel channel3 = new Channel("채널 3번", "백엔드 디코방");

        // 생성
        ChannelDTO channel1DTO = channelService.save(channel1);
        ChannelDTO channel2DTO = channelService.save(channel2);
        ChannelDTO channel3DTO = channelService.save(channel3);
        System.out.println("채널 생성 = " + channel1DTO);
        System.out.println("채널 생성 = " + channel2DTO);
        System.out.println("채널 생성 = " + channel3DTO);

        // 조회 [단건]
        ChannelDTO find1 = channelService.findById(channel1.getId());
        ChannelDTO find2 = channelService.findById(UUID.randomUUID()); // 없는 ID로 조회
        System.out.println("채널[단건] :" + find1);
        System.out.println("채널 조회 [단건] - 없는 ID로 조회: " + find2);

        // 조회 [다건]
        List<ChannelDTO> findAll = channelService.findAll();
        System.out.println(findAll);

        // 수정
        channel1.update("채널 1번수정", "디자이너 디코방수정");
        ChannelDTO modifiedChannel = channelService.modify(channel1);
        System.out.println("수정된 채널: " + modifiedChannel);
        ChannelDTO modifiedChk = channelService.findById(channel1.getId());
        System.out.println("채널 수정 확인: " + modifiedChk);

        // 삭제
        ChannelDTO deletedChannel = channelService.deleteById(channel1.getId());
        System.out.println("삭제된 채널: " + deletedChannel);
        ChannelDTO deletedChk = channelService.findById(channel1.getId());
        System.out.println("채널 삭제 확인: " + deletedChk);
    }

    public static void messageCRUDTest(MessageService messageService) {
        System.out.println("\n메세지 테스트");
        User user1 = new User("홍길동", "홍길동 이메일.com", "1234");
        User user2 = new User("이순신", "이순신 이메일.com", "5678");

        Channel channel1 = new Channel("채널 1번", "프론트엔드 디코방");
        Channel channel2 = new Channel("채널 2번", "백엔드 디코방");

        Message message1 = new Message("메세지 내용입니다! 111111111", user1.getId(), channel1.getId());
        Message message2 = new Message("메세지 내용입니다! 222222222", user1.getId(), channel2.getId());
        Message message3 = new Message("메세지 내용입니다! 333333333", user2.getId(), channel1.getId());

        // 등록
        MessageDTO message1DTO = messageService.save(message1);
        MessageDTO message2DTO = messageService.save(message2);
        MessageDTO message3DTO = messageService.save(message3);
        System.out.println("메세지 생성: " + message1DTO);
        System.out.println("메세지 생성: " + message2DTO);
        System.out.println("메세지 생성: " + message3DTO);

        // 조회
        MessageDTO find1 = messageService.findById(message1.getId());
        MessageDTO find2 = messageService.findById(UUID.randomUUID());
        System.out.println("메세지 조회 [단건]: " + find1);
        System.out.println("메세지 조회 [단건] - 없는 ID로 조회: " + find2);

        // 조회 [다건]
        List<MessageDTO> findAll = messageService.findAll();
        System.out.println("메세지 조회[다건]: " + findAll);

        // 수정
        message1.update("메세지 내용입니다! 111111111 수정수정");
        MessageDTO updatedMessage = messageService.modify(message1);
        System.out.println("수정된 메세지: " + updatedMessage);
        MessageDTO updatedMessageChk = messageService.findById(message1.getId());
        System.out.println("메세지 수정 확인: " + updatedMessageChk);

        // 삭제
        MessageDTO deleteMessage = messageService.deleteById(message1.getId());
        System.out.println("삭제된 메세지: " + deleteMessage);
        MessageDTO deletedMessageChk = messageService.findById(message1.getId());
        System.out.println("메세지 삭제 확인: " + deletedMessageChk);
    }


    public static void main(String[] args) {

        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService);
    }
}
