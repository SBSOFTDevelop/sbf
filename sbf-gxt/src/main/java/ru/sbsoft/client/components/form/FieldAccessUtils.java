package ru.sbsoft.client.components.form;

//НЕ ФОРМАТИРОВАТЬ!


/**
Позволяет получить доступ к полям объекта по их названию в runtime (что-то вроде reflection).
@author Fedor Resnyanskiy, SBSOFT
*/
public class FieldAccessUtils {

    public static final native Object getValue(Object object, String field)/*-{
        if(typeof object[field] == "undefined"){
           //такого поля нет; возможно, GWT добавил суффикс. Надо перебрать все поля.
           //ниже: return getValueForSuffixedField(object, field);
           return @ru.sbsoft.client.components.form.FieldAccessUtils::getValueForSuffixedField(Ljava/lang/Object;Ljava/lang/String;)(object, field);
        }
        return object[field];
     }-*/;

    //-----------------------------------------------------------------------------------------
    
    /**
     Поиск по всем полям объекта.
     GWT добавляет в названия полей суффикс при конвертации в JS. 
     Чтобы выбрать правильное поле, надо передбать их все.
     */
    private static native Object getValueForSuffixedField(Object object, String field)/*-{ 
        var fields = Object.getOwnPropertyNames(object).filter(function(property) {
            return typeof object[property] != 'function' && property.match(field+'_[0-9]+');
        });
        return object[fields[0]];
     }-*/;
    
    //-----------------------------------------------------------------------------------------
    
}
