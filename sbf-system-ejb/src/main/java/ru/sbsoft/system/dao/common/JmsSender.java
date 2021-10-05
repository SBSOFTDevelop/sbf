package ru.sbsoft.system.dao.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

/**
 *
 * @author sychugin
 */
@ApplicationScoped
public class JmsSender {

    private Logger LOG = Logger.getLogger(JmsSender.class.getName());

    @Resource(mappedName = ru.sbsoft.common.jdbc.Const.JMS_QUEUE)
    private Queue queue;

    @Inject
    private JMSContext context;

    
    public void sendMessage(long operId, String opCode) {

        try {
            
            Message message = context.createMessage();
            message.setLongProperty(ru.sbsoft.common.jdbc.Const.OPER_ID, operId);
            message.setStringProperty(ru.sbsoft.common.jdbc.Const.OPER_CODE, opCode);
            context.createProducer().send(queue, message);
        } catch (JMSException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
