package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmFieldType;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayMenuRealmProxy extends com.nunoneto.assicanti.model.DayMenu
    implements RealmObjectProxy, DayMenuRealmProxyInterface {

    static final class DayMenuColumnInfo extends ColumnInfo {

        public final long dayOfWeekIndex;
        public final long descriptionIndex;

        DayMenuColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(2);
            this.dayOfWeekIndex = getValidColumnIndex(path, table, "DayMenu", "dayOfWeek");
            indicesMap.put("dayOfWeek", this.dayOfWeekIndex);

            this.descriptionIndex = getValidColumnIndex(path, table, "DayMenu", "description");
            indicesMap.put("description", this.descriptionIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final DayMenuColumnInfo columnInfo;
    private final ProxyState proxyState;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("dayOfWeek");
        fieldNames.add("description");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    DayMenuRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (DayMenuColumnInfo) columnInfo;
        this.proxyState = new ProxyState(com.nunoneto.assicanti.model.DayMenu.class, this);
    }

    @SuppressWarnings("cast")
    public int realmGet$dayOfWeek() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.dayOfWeekIndex);
    }

    public void realmSet$dayOfWeek(int value) {
        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.dayOfWeekIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$description() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descriptionIndex);
    }

    public void realmSet$description(String value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descriptionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descriptionIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_DayMenu")) {
            Table table = transaction.getTable("class_DayMenu");
            table.addColumn(RealmFieldType.INTEGER, "dayOfWeek", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "description", Table.NULLABLE);
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_DayMenu");
    }

    public static DayMenuColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_DayMenu")) {
            Table table = transaction.getTable("class_DayMenu");
            if (table.getColumnCount() != 2) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 2 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final DayMenuColumnInfo columnInfo = new DayMenuColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("dayOfWeek")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'dayOfWeek' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("dayOfWeek") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'dayOfWeek' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.dayOfWeekIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'dayOfWeek' does support null values in the existing Realm file. Use corresponding boxed type for field 'dayOfWeek' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("description")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'description' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("description") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'description' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.descriptionIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'description' is required. Either set @Required to field 'description' or migrate using RealmObjectSchema.setNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The DayMenu class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_DayMenu";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.nunoneto.assicanti.model.DayMenu createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        com.nunoneto.assicanti.model.DayMenu obj = realm.createObject(com.nunoneto.assicanti.model.DayMenu.class);
        if (json.has("dayOfWeek")) {
            if (json.isNull("dayOfWeek")) {
                throw new IllegalArgumentException("Trying to set non-nullable field dayOfWeek to null.");
            } else {
                ((DayMenuRealmProxyInterface) obj).realmSet$dayOfWeek((int) json.getInt("dayOfWeek"));
            }
        }
        if (json.has("description")) {
            if (json.isNull("description")) {
                ((DayMenuRealmProxyInterface) obj).realmSet$description(null);
            } else {
                ((DayMenuRealmProxyInterface) obj).realmSet$description((String) json.getString("description"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static com.nunoneto.assicanti.model.DayMenu createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.nunoneto.assicanti.model.DayMenu obj = realm.createObject(com.nunoneto.assicanti.model.DayMenu.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("dayOfWeek")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field dayOfWeek to null.");
                } else {
                    ((DayMenuRealmProxyInterface) obj).realmSet$dayOfWeek((int) reader.nextInt());
                }
            } else if (name.equals("description")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((DayMenuRealmProxyInterface) obj).realmSet$description(null);
                } else {
                    ((DayMenuRealmProxyInterface) obj).realmSet$description((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static com.nunoneto.assicanti.model.DayMenu copyOrUpdate(Realm realm, com.nunoneto.assicanti.model.DayMenu object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.nunoneto.assicanti.model.DayMenu) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.nunoneto.assicanti.model.DayMenu copy(Realm realm, com.nunoneto.assicanti.model.DayMenu newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.nunoneto.assicanti.model.DayMenu) cachedRealmObject;
        } else {
            com.nunoneto.assicanti.model.DayMenu realmObject = realm.createObject(com.nunoneto.assicanti.model.DayMenu.class);
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((DayMenuRealmProxyInterface) realmObject).realmSet$dayOfWeek(((DayMenuRealmProxyInterface) newObject).realmGet$dayOfWeek());
            ((DayMenuRealmProxyInterface) realmObject).realmSet$description(((DayMenuRealmProxyInterface) newObject).realmGet$description());
            return realmObject;
        }
    }

    public static long insert(Realm realm, com.nunoneto.assicanti.model.DayMenu object, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.DayMenu.class);
        long tableNativePtr = table.getNativeTablePointer();
        DayMenuColumnInfo columnInfo = (DayMenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.DayMenu.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.dayOfWeekIndex, rowIndex, ((DayMenuRealmProxyInterface)object).realmGet$dayOfWeek());
        String realmGet$description = ((DayMenuRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.DayMenu.class);
        long tableNativePtr = table.getNativeTablePointer();
        DayMenuColumnInfo columnInfo = (DayMenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.DayMenu.class);
        com.nunoneto.assicanti.model.DayMenu object = null;
        while (objects.hasNext()) {
            object = (com.nunoneto.assicanti.model.DayMenu) objects.next();
            if(!cache.containsKey(object)) {
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.dayOfWeekIndex, rowIndex, ((DayMenuRealmProxyInterface)object).realmGet$dayOfWeek());
                String realmGet$description = ((DayMenuRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.nunoneto.assicanti.model.DayMenu object, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.DayMenu.class);
        long tableNativePtr = table.getNativeTablePointer();
        DayMenuColumnInfo columnInfo = (DayMenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.DayMenu.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.dayOfWeekIndex, rowIndex, ((DayMenuRealmProxyInterface)object).realmGet$dayOfWeek());
        String realmGet$description = ((DayMenuRealmProxyInterface)object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.DayMenu.class);
        long tableNativePtr = table.getNativeTablePointer();
        DayMenuColumnInfo columnInfo = (DayMenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.DayMenu.class);
        com.nunoneto.assicanti.model.DayMenu object = null;
        while (objects.hasNext()) {
            object = (com.nunoneto.assicanti.model.DayMenu) objects.next();
            if(!cache.containsKey(object)) {
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                Table.nativeSetLong(tableNativePtr, columnInfo.dayOfWeekIndex, rowIndex, ((DayMenuRealmProxyInterface)object).realmGet$dayOfWeek());
                String realmGet$description = ((DayMenuRealmProxyInterface)object).realmGet$description();
                if (realmGet$description != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex);
                }
            }
        }
    }

    public static com.nunoneto.assicanti.model.DayMenu createDetachedCopy(com.nunoneto.assicanti.model.DayMenu realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.nunoneto.assicanti.model.DayMenu unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.nunoneto.assicanti.model.DayMenu)cachedObject.object;
            } else {
                unmanagedObject = (com.nunoneto.assicanti.model.DayMenu)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.nunoneto.assicanti.model.DayMenu();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((DayMenuRealmProxyInterface) unmanagedObject).realmSet$dayOfWeek(((DayMenuRealmProxyInterface) realmObject).realmGet$dayOfWeek());
        ((DayMenuRealmProxyInterface) unmanagedObject).realmSet$description(((DayMenuRealmProxyInterface) realmObject).realmGet$description());
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("DayMenu = [");
        stringBuilder.append("{dayOfWeek:");
        stringBuilder.append(realmGet$dayOfWeek());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{description:");
        stringBuilder.append(realmGet$description() != null ? realmGet$description() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayMenuRealmProxy aDayMenu = (DayMenuRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aDayMenu.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aDayMenu.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aDayMenu.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
