package ru.sbsoft.shared.interfaces;

/**
 * Описание ошибки целостности данных {@link java.sql.SQLIntegrityConstraintViolationException} БД для выдачи ее в виде понятного сообщения.
 * При этом {@link ObjectType#getCode()} ищется как подстрока в сообщении с сервера и, если найдено, выдается сообщение о нарушении целостности, содержащее имя констрейнта ({@link ConstraintType#getTitle()})
 * @see ru.sbsoft.client.components.form.BaseForm#addUniqConstraintInfo(final ConstraintType type)
 * @author balandin
 * @since Jun 26, 2013 4:22:08 PM
 */
public interface ConstraintType extends ObjectType {

    String getTitle();
}
