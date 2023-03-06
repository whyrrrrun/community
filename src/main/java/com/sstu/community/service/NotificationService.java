package com.sstu.community.service;

import com.sstu.community.dto.NotificationDTO;
import com.sstu.community.dto.PaginationDTO;
import com.sstu.community.enums.NotificationStatusEnum;
import com.sstu.community.enums.NotificationTypeEnum;
import com.sstu.community.exception.CustomizeErrorCode;
import com.sstu.community.exception.CustomizeException;
import com.sstu.community.mapper.CommentMapper;
import com.sstu.community.mapper.NotificationMapper;
import com.sstu.community.mapper.QuestionMapper;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {


    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;


    public PaginationDTO<NotificationDTO> list(Long id, Integer page, Integer size) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        Integer idCount = (int) notificationMapper.countByExample(notificationExample);

        if(page < 1)
            page = 1;
        Integer offset = size * (page - 1);
        if(offset > idCount)
            offset = idCount - (idCount % size);

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds(offset,size));
        if(notifications.size() == 0)
            return paginationDTO;
        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            User user = userMapper.selectByPrimaryKey(notification.getReceiver());
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setNotifier(user);
            if(notification.getType() == 1)
                notificationDTO.setType(NotificationTypeEnum.REPLY_QUESTION.getMessage());
            else if(notification.getType() == 2)
                notificationDTO.setType(NotificationTypeEnum.REPLY_COMMENT.getMessage());
            if(notification.getType() == 1){
                notificationDTO.setOuterTitle(questionMapper.selectByPrimaryKey(notification.getOuterId()).getTitle());
            }else {
                notificationDTO.setOuterTitle(commentMapper.selectByPrimaryKey(notification.getOuterId()).getContent());
            }
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(page,size,idCount);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null)
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

}
