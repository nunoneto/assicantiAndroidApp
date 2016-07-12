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

public class MenuRealmProxy extends com.nunoneto.assicanti.model.Menu
    implements RealmObjectProxy, MenuRealmProxyInterface {

    static final class MenuColumnInfo extends ColumnInfo {

        public final long startingIndex;
        public final long endingIndex;
        public final long typeIndex;
        public final long daysIndex;

        MenuColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(4);
            this.startingIndex = getValidColumnIndex(path, table, "Menu", "starting");
            indicesMap.put("starting", this.startingIndex);

            this.endingIndex = getValidColumnIndex(path, table, "Menu", "ending");
            indicesMap.put("ending", this.endingIndex);

            this.typeIndex = getValidColumnIndex(path, table, "Menu", "type");
            indicesMap.put("type", this.typeIndex);

            this.daysIndex = getValidColumnIndex(path, table, "Menu", "days");
            indicesMap.put("days", this.daysIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final MenuColumnInfo columnInfo;
    private final ProxyState proxyState;
    private RealmList<com.nunoneto.assicanti.model.DayMenu> daysRealmList;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("starting");
        fieldNames.add("ending");
        fieldNames.add("type");
        fieldNames.add("days");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    MenuRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (MenuColumnInfo) columnInfo;
        this.proxyState = new ProxyState(com.nunoneto.assicanti.model.Menu.class, this);
    }

    @SuppressWarnings("cast")
    public Date realmGet$starting() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.startingIndex)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.startingIndex);
    }

    public void realmSet$starting(Date value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.startingIndex);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.startingIndex, value);
    }

    @SuppressWarnings("cast")
    public Date realmGet$ending() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNull(columnInfo.endingIndex)) {
            return null;
        }
        return (java.util.Date) proxyState.getRow$realm().getDate(columnInfo.endingIndex);
    }

    public void realmSet$ending(Date value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.endingIndex);
            return;
        }
        proxyState.getRow$realm().setDate(columnInfo.endingIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.typeIndex);
    }

    public void realmSet$type(String value) {
        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.typeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.typeIndex, value);
    }

    public RealmList<com.nunoneto.assicanti.model.DayMenu> realmGet$days() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (daysRealmList != null) {
            return daysRealmList;
        } else {
            LinkView linkView = proxyState.getRow$realm().getLinkList(columnInfo.daysIndex);
            daysRealmList = new RealmList<com.nunoneto.assicanti.model.DayMenu>(com.nunoneto.assicanti.model.DayMenu.class, linkView, proxyState.getRealm$realm());
            return daysRealmList;
        }
    }

    public void realmSet$days(RealmList<com.nunoneto.assicanti.model.DayMenu> value) {
        proxyState.getRealm$realm().checkIfValid();
        LinkView links = proxyState.getRow$realm().getLinkList(columnInfo.daysIndex);
        links.clear();
        if (value == null) {
            return;
        }
        for (RealmModel linkedObject : (RealmList<? extends RealmModel>) value) {
            if (!RealmObject.isValid(linkedObject)) {
                throw new IllegalArgumentException("Each element of 'value' must be a valid managed object.");
            }
            if (((RealmObjectProxy)linkedObject).realmGet$proxyState().getRealm$realm() != proxyState.getRealm$realm()) {
                throw new IllegalArgumentException("Each element of 'value' must belong to the same Realm.");
            }
            links.add(((RealmObjectProxy)linkedObject).realmGet$proxyState().getRow$realm().getIndex());
        }
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_Menu")) {
            Table table = transaction.getTable("class_Menu");
            table.addColumn(RealmFieldType.DATE, "starting", Table.NULLABLE);
            table.addColumn(RealmFieldType.DATE, "ending", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "type", Table.NULLABLE);
            if (!transaction.hasTable("class_DayMenu")) {
                DayMenuRealmProxy.initTable(transaction);
            }
            table.addColumnLink(RealmFieldType.LIST, "days", transaction.getTable("class_DayMenu"));
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_Menu");
    }

    public static MenuColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Menu")) {
            Table table = transaction.getTable("class_Menu");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final MenuColumnInfo columnInfo = new MenuColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("starting")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'starting' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("starting") != RealmFieldType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'starting' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.startingIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'starting' is required. Either set @Required to field 'starting' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("ending")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'ending' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("ending") != RealmFieldType.DATE) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Date' for field 'ending' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.endingIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'ending' is required. Either set @Required to field 'ending' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("type")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'type' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("type") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'type' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.typeIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'type' is required. Either set @Required to field 'type' or migrate using RealmObjectSchema.setNullable().");
            }
            if (!columnTypes.containsKey("days")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'days'");
            }
            if (columnTypes.get("days") != RealmFieldType.LIST) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'DayMenu' for field 'days'");
            }
            if (!transaction.hasTable("class_DayMenu")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing class 'class_DayMenu' for field 'days'");
            }
            Table table_3 = transaction.getTable("class_DayMenu");
            if (!table.getLinkTarget(columnInfo.daysIndex).hasSameSchema(table_3)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid RealmList type for field 'days': '" + table.getLinkTarget(columnInfo.daysIndex).getName() + "' expected - was '" + table_3.getName() + "'");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The Menu class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Menu";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static com.nunoneto.assicanti.model.Menu createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        com.nunoneto.assicanti.model.Menu obj = realm.createObject(com.nunoneto.assicanti.model.Menu.class);
        if (json.has("starting")) {
            if (json.isNull("starting")) {
                ((MenuRealmProxyInterface) obj).realmSet$starting(null);
            } else {
                Object timestamp = json.get("starting");
                if (timestamp instanceof String) {
                    ((MenuRealmProxyInterface) obj).realmSet$starting(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((MenuRealmProxyInterface) obj).realmSet$starting(new Date(json.getLong("starting")));
                }
            }
        }
        if (json.has("ending")) {
            if (json.isNull("ending")) {
                ((MenuRealmProxyInterface) obj).realmSet$ending(null);
            } else {
                Object timestamp = json.get("ending");
                if (timestamp instanceof String) {
                    ((MenuRealmProxyInterface) obj).realmSet$ending(JsonUtils.stringToDate((String) timestamp));
                } else {
                    ((MenuRealmProxyInterface) obj).realmSet$ending(new Date(json.getLong("ending")));
                }
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                ((MenuRealmProxyInterface) obj).realmSet$type(null);
            } else {
                ((MenuRealmProxyInterface) obj).realmSet$type((String) json.getString("type"));
            }
        }
        if (json.has("days")) {
            if (json.isNull("days")) {
                ((MenuRealmProxyInterface) obj).realmSet$days(null);
            } else {
                ((MenuRealmProxyInterface) obj).realmGet$days().clear();
                JSONArray array = json.getJSONArray("days");
                for (int i = 0; i < array.length(); i++) {
                    com.nunoneto.assicanti.model.DayMenu item = DayMenuRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    ((MenuRealmProxyInterface) obj).realmGet$days().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static com.nunoneto.assicanti.model.Menu createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        com.nunoneto.assicanti.model.Menu obj = realm.createObject(com.nunoneto.assicanti.model.Menu.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("starting")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MenuRealmProxyInterface) obj).realmSet$starting(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((MenuRealmProxyInterface) obj).realmSet$starting(new Date(timestamp));
                    }
                } else {
                    ((MenuRealmProxyInterface) obj).realmSet$starting(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("ending")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MenuRealmProxyInterface) obj).realmSet$ending(null);
                } else if (reader.peek() == JsonToken.NUMBER) {
                    long timestamp = reader.nextLong();
                    if (timestamp > -1) {
                        ((MenuRealmProxyInterface) obj).realmSet$ending(new Date(timestamp));
                    }
                } else {
                    ((MenuRealmProxyInterface) obj).realmSet$ending(JsonUtils.stringToDate(reader.nextString()));
                }
            } else if (name.equals("type")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MenuRealmProxyInterface) obj).realmSet$type(null);
                } else {
                    ((MenuRealmProxyInterface) obj).realmSet$type((String) reader.nextString());
                }
            } else if (name.equals("days")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((MenuRealmProxyInterface) obj).realmSet$days(null);
                } else {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.nunoneto.assicanti.model.DayMenu item = DayMenuRealmProxy.createUsingJsonStream(realm, reader);
                        ((MenuRealmProxyInterface) obj).realmGet$days().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static com.nunoneto.assicanti.model.Menu copyOrUpdate(Realm realm, com.nunoneto.assicanti.model.Menu object, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy)object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return object;
        }
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.nunoneto.assicanti.model.Menu) cachedRealmObject;
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static com.nunoneto.assicanti.model.Menu copy(Realm realm, com.nunoneto.assicanti.model.Menu newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.nunoneto.assicanti.model.Menu) cachedRealmObject;
        } else {
            com.nunoneto.assicanti.model.Menu realmObject = realm.createObject(com.nunoneto.assicanti.model.Menu.class);
            cache.put(newObject, (RealmObjectProxy) realmObject);
            ((MenuRealmProxyInterface) realmObject).realmSet$starting(((MenuRealmProxyInterface) newObject).realmGet$starting());
            ((MenuRealmProxyInterface) realmObject).realmSet$ending(((MenuRealmProxyInterface) newObject).realmGet$ending());
            ((MenuRealmProxyInterface) realmObject).realmSet$type(((MenuRealmProxyInterface) newObject).realmGet$type());

            RealmList<com.nunoneto.assicanti.model.DayMenu> daysList = ((MenuRealmProxyInterface) newObject).realmGet$days();
            if (daysList != null) {
                RealmList<com.nunoneto.assicanti.model.DayMenu> daysRealmList = ((MenuRealmProxyInterface) realmObject).realmGet$days();
                for (int i = 0; i < daysList.size(); i++) {
                    com.nunoneto.assicanti.model.DayMenu daysItem = daysList.get(i);
                    com.nunoneto.assicanti.model.DayMenu cachedays = (com.nunoneto.assicanti.model.DayMenu) cache.get(daysItem);
                    if (cachedays != null) {
                        daysRealmList.add(cachedays);
                    } else {
                        daysRealmList.add(DayMenuRealmProxy.copyOrUpdate(realm, daysList.get(i), update, cache));
                    }
                }
            }

            return realmObject;
        }
    }

    public static long insert(Realm realm, com.nunoneto.assicanti.model.Menu object, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.Menu.class);
        long tableNativePtr = table.getNativeTablePointer();
        MenuColumnInfo columnInfo = (MenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.Menu.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        java.util.Date realmGet$starting = ((MenuRealmProxyInterface)object).realmGet$starting();
        if (realmGet$starting != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.startingIndex, rowIndex, realmGet$starting.getTime());
        }
        java.util.Date realmGet$ending = ((MenuRealmProxyInterface)object).realmGet$ending();
        if (realmGet$ending != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.endingIndex, rowIndex, realmGet$ending.getTime());
        }
        String realmGet$type = ((MenuRealmProxyInterface)object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type);
        }

        RealmList<com.nunoneto.assicanti.model.DayMenu> daysList = ((MenuRealmProxyInterface) object).realmGet$days();
        if (daysList != null) {
            long daysNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.daysIndex, rowIndex);
            for (com.nunoneto.assicanti.model.DayMenu daysItem : daysList) {
                Long cacheItemIndexdays = cache.get(daysItem);
                if (cacheItemIndexdays == null) {
                    cacheItemIndexdays = DayMenuRealmProxy.insert(realm, daysItem, cache);
                }
                LinkView.nativeAdd(daysNativeLinkViewPtr, cacheItemIndexdays);
            }
            LinkView.nativeClose(daysNativeLinkViewPtr);
        }

        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.Menu.class);
        long tableNativePtr = table.getNativeTablePointer();
        MenuColumnInfo columnInfo = (MenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.Menu.class);
        com.nunoneto.assicanti.model.Menu object = null;
        while (objects.hasNext()) {
            object = (com.nunoneto.assicanti.model.Menu) objects.next();
            if(!cache.containsKey(object)) {
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                java.util.Date realmGet$starting = ((MenuRealmProxyInterface)object).realmGet$starting();
                if (realmGet$starting != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.startingIndex, rowIndex, realmGet$starting.getTime());
                }
                java.util.Date realmGet$ending = ((MenuRealmProxyInterface)object).realmGet$ending();
                if (realmGet$ending != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.endingIndex, rowIndex, realmGet$ending.getTime());
                }
                String realmGet$type = ((MenuRealmProxyInterface)object).realmGet$type();
                if (realmGet$type != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type);
                }

                RealmList<com.nunoneto.assicanti.model.DayMenu> daysList = ((MenuRealmProxyInterface) object).realmGet$days();
                if (daysList != null) {
                    long daysNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.daysIndex, rowIndex);
                    for (com.nunoneto.assicanti.model.DayMenu daysItem : daysList) {
                        Long cacheItemIndexdays = cache.get(daysItem);
                        if (cacheItemIndexdays == null) {
                            cacheItemIndexdays = DayMenuRealmProxy.insert(realm, daysItem, cache);
                        }
                        LinkView.nativeAdd(daysNativeLinkViewPtr, cacheItemIndexdays);
                    }
                    LinkView.nativeClose(daysNativeLinkViewPtr);
                }

            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.nunoneto.assicanti.model.Menu object, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.Menu.class);
        long tableNativePtr = table.getNativeTablePointer();
        MenuColumnInfo columnInfo = (MenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.Menu.class);
        long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
        cache.put(object, rowIndex);
        java.util.Date realmGet$starting = ((MenuRealmProxyInterface)object).realmGet$starting();
        if (realmGet$starting != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.startingIndex, rowIndex, realmGet$starting.getTime());
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.startingIndex, rowIndex);
        }
        java.util.Date realmGet$ending = ((MenuRealmProxyInterface)object).realmGet$ending();
        if (realmGet$ending != null) {
            Table.nativeSetTimestamp(tableNativePtr, columnInfo.endingIndex, rowIndex, realmGet$ending.getTime());
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.endingIndex, rowIndex);
        }
        String realmGet$type = ((MenuRealmProxyInterface)object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex);
        }

        long daysNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.daysIndex, rowIndex);
        LinkView.nativeClear(daysNativeLinkViewPtr);
        RealmList<com.nunoneto.assicanti.model.DayMenu> daysList = ((MenuRealmProxyInterface) object).realmGet$days();
        if (daysList != null) {
            for (com.nunoneto.assicanti.model.DayMenu daysItem : daysList) {
                Long cacheItemIndexdays = cache.get(daysItem);
                if (cacheItemIndexdays == null) {
                    cacheItemIndexdays = DayMenuRealmProxy.insertOrUpdate(realm, daysItem, cache);
                }
                LinkView.nativeAdd(daysNativeLinkViewPtr, cacheItemIndexdays);
            }
        }
        LinkView.nativeClose(daysNativeLinkViewPtr);

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.nunoneto.assicanti.model.Menu.class);
        long tableNativePtr = table.getNativeTablePointer();
        MenuColumnInfo columnInfo = (MenuColumnInfo) realm.schema.getColumnInfo(com.nunoneto.assicanti.model.Menu.class);
        com.nunoneto.assicanti.model.Menu object = null;
        while (objects.hasNext()) {
            object = (com.nunoneto.assicanti.model.Menu) objects.next();
            if(!cache.containsKey(object)) {
                long rowIndex = Table.nativeAddEmptyRow(tableNativePtr, 1);
                cache.put(object, rowIndex);
                java.util.Date realmGet$starting = ((MenuRealmProxyInterface)object).realmGet$starting();
                if (realmGet$starting != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.startingIndex, rowIndex, realmGet$starting.getTime());
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.startingIndex, rowIndex);
                }
                java.util.Date realmGet$ending = ((MenuRealmProxyInterface)object).realmGet$ending();
                if (realmGet$ending != null) {
                    Table.nativeSetTimestamp(tableNativePtr, columnInfo.endingIndex, rowIndex, realmGet$ending.getTime());
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.endingIndex, rowIndex);
                }
                String realmGet$type = ((MenuRealmProxyInterface)object).realmGet$type();
                if (realmGet$type != null) {
                    Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type);
                } else {
                    Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex);
                }

                long daysNativeLinkViewPtr = Table.nativeGetLinkView(tableNativePtr, columnInfo.daysIndex, rowIndex);
                LinkView.nativeClear(daysNativeLinkViewPtr);
                RealmList<com.nunoneto.assicanti.model.DayMenu> daysList = ((MenuRealmProxyInterface) object).realmGet$days();
                if (daysList != null) {
                    for (com.nunoneto.assicanti.model.DayMenu daysItem : daysList) {
                        Long cacheItemIndexdays = cache.get(daysItem);
                        if (cacheItemIndexdays == null) {
                            cacheItemIndexdays = DayMenuRealmProxy.insertOrUpdate(realm, daysItem, cache);
                        }
                        LinkView.nativeAdd(daysNativeLinkViewPtr, cacheItemIndexdays);
                    }
                }
                LinkView.nativeClose(daysNativeLinkViewPtr);

            }
        }
    }

    public static com.nunoneto.assicanti.model.Menu createDetachedCopy(com.nunoneto.assicanti.model.Menu realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.nunoneto.assicanti.model.Menu unmanagedObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.nunoneto.assicanti.model.Menu)cachedObject.object;
            } else {
                unmanagedObject = (com.nunoneto.assicanti.model.Menu)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            unmanagedObject = new com.nunoneto.assicanti.model.Menu();
            cache.put(realmObject, new RealmObjectProxy.CacheData(currentDepth, unmanagedObject));
        }
        ((MenuRealmProxyInterface) unmanagedObject).realmSet$starting(((MenuRealmProxyInterface) realmObject).realmGet$starting());
        ((MenuRealmProxyInterface) unmanagedObject).realmSet$ending(((MenuRealmProxyInterface) realmObject).realmGet$ending());
        ((MenuRealmProxyInterface) unmanagedObject).realmSet$type(((MenuRealmProxyInterface) realmObject).realmGet$type());

        // Deep copy of days
        if (currentDepth == maxDepth) {
            ((MenuRealmProxyInterface) unmanagedObject).realmSet$days(null);
        } else {
            RealmList<com.nunoneto.assicanti.model.DayMenu> manageddaysList = ((MenuRealmProxyInterface) realmObject).realmGet$days();
            RealmList<com.nunoneto.assicanti.model.DayMenu> unmanageddaysList = new RealmList<com.nunoneto.assicanti.model.DayMenu>();
            ((MenuRealmProxyInterface) unmanagedObject).realmSet$days(unmanageddaysList);
            int nextDepth = currentDepth + 1;
            int size = manageddaysList.size();
            for (int i = 0; i < size; i++) {
                com.nunoneto.assicanti.model.DayMenu item = DayMenuRealmProxy.createDetachedCopy(manageddaysList.get(i), nextDepth, maxDepth, cache);
                unmanageddaysList.add(item);
            }
        }
        return unmanagedObject;
    }

    @Override
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Menu = [");
        stringBuilder.append("{starting:");
        stringBuilder.append(realmGet$starting() != null ? realmGet$starting() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ending:");
        stringBuilder.append(realmGet$ending() != null ? realmGet$ending() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{days:");
        stringBuilder.append("RealmList<DayMenu>[").append(realmGet$days().size()).append("]");
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
        MenuRealmProxy aMenu = (MenuRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aMenu.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aMenu.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aMenu.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }

}
