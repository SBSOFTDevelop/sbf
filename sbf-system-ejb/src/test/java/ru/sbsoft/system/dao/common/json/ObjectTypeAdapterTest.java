package ru.sbsoft.system.dao.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import ru.sbsoft.shared.interfaces.DynamicGridType;
import ru.sbsoft.shared.interfaces.IDynamicGridType;
import ru.sbsoft.shared.interfaces.ObjectType;

/**
 *
 * @author vk
 */
public class ObjectTypeAdapterTest {

    @Test
    public void testDynamicGridType() {
        final Gson g = new GsonBuilder()
                .registerTypeHierarchyAdapter(ObjectType.class, new ObjectTypeAdapter())
                .create();
        AAA a = new AAA(12, new DynamicGridType(() -> "COOL_CODE", BigDecimal.valueOf(126), "Test name"), "Simple string");
        String json = g.toJson(a);
        System.out.println(json);
        AAA a2 = g.fromJson(json, AAA.class);
        System.out.println(a2);
        Assert.assertEquals(a, a2);
        System.out.print("itemName class: ");
        System.out.println(((IDynamicGridType) a2.getTtt()).getItemName().getClass().getName());
    }

    private class AAA {

        private final int mmm;
        private final ObjectType ttt;
        private final String vvv;

        public AAA(int mmm, ObjectType ttt, String vvv) {
            this.mmm = mmm;
            this.ttt = ttt;
            this.vvv = vvv;
        }

        public int getMmm() {
            return mmm;
        }

        public ObjectType getTtt() {
            return ttt;
        }

        public String getVvv() {
            return vvv;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + this.mmm;
            hash = 97 * hash + Objects.hashCode(this.ttt);
            hash = 97 * hash + Objects.hashCode(this.vvv);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final AAA other = (AAA) obj;
            return this.mmm == other.mmm && Objects.equals(this.vvv, other.vvv) && Objects.equals(this.ttt, other.ttt);
        }

        @Override
        public String toString() {
            return "AAA{" + "mmm=" + mmm + ", ttt=" + ttt + ", vvv=" + vvv + '}';
        }
    }
}
