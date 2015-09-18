package com.bitdubai.fermat_dap_plugin.layer.middleware.asset.issuer.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.layer.all_definition.enums.WalletCategory;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletType;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ResourceDensity;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ResourceType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_dap_api.layer.all_definition.contracts.Contract;
import com.bitdubai.fermat_dap_api.layer.all_definition.contracts.ContractProperty;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetContract;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.State;
import com.bitdubai.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import com.bitdubai.fermat_dap_plugin.layer.middleware.asset.issuer.developer.bitdubai.version_1.exceptions.DatabaseOperationException;
import com.bitdubai.fermat_dap_plugin.layer.middleware.asset.issuer.developer.bitdubai.version_1.exceptions.MissingAssetDataException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 15/09/15.
 */
public class AssetFactoryMiddlewareDao implements DealsWithPluginDatabaseSystem {
    Database database;
    UUID pluginId;
    /**
     * DealsWithPluginDatabaseSystem interface variable and implementation
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Constructor
     */
    public AssetFactoryMiddlewareDao(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    private DatabaseTable getDatabaseTable(String tableName) {
        DatabaseTable databaseTable = database.getTable(tableName);
        return databaseTable;
    }

    private Database openDatabase() throws CantOpenDatabaseException, CantCreateDatabaseException {
        try {
            database = pluginDatabaseSystem.openDatabase(this.pluginId, AssertFactoryMiddlewareDatabaseConstant.DATABASE_NAME);

        } catch (DatabaseNotFoundException e) {
            AssetFactoryMiddlewareDatabaseFactory assetFactoryMiddlewareDatabaseFactory = new AssetFactoryMiddlewareDatabaseFactory(this.pluginDatabaseSystem);
            database = assetFactoryMiddlewareDatabaseFactory.createDatabase(this.pluginId, AssertFactoryMiddlewareDatabaseConstant.DATABASE_NAME);
        }
        return database;
    }

    private DatabaseTableRecord getAssetFactoryProjectRecord(AssetFactory assetFactory) throws DatabaseOperationException {
        DatabaseTable databaseTable = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_TABLE_NAME);
        DatabaseTableRecord record = databaseTable.getEmptyRecord();

        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_ASSET_PUBLIC_KEY_COLUMN, assetFactory.getPublicKey());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_NAME_COLUMN, assetFactory.getName());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_DESCRIPTION_COLUMN, assetFactory.getDescription());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_ISSUER_IDENTITY_PUBLIC_KEY_COLUMN, assetFactory.getAssetIssuerIdentityPublicKey());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_STATE_COLUMN, assetFactory.getState().getCode());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_CATEGORY_COLUMN, assetFactory.getWalletCategory().getCode());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_TYPE_COLUMN, assetFactory.getWalletType().getCode());
        record.setLongValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_FEE_COLUMN, assetFactory.getFee());
        record.setLongValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_AMOUNT_COLUMN, assetFactory.getAmount());
        record.setLongValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_QUANTITY_COLUMN, assetFactory.getQuantity());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CREATION_TIME_COLUMN, assetFactory.getCreationTimestamp().toString());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_LAST_UPDATE_TIME_COLUMN, assetFactory.getLastModificationTimestamp().toString());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_PUBLIC_KEY_COLUMN, assetFactory.getWalletPublicKey().toString());
        //TODO: Falta Genesis Transaction

        return record;
    }

    private DatabaseTableRecord getResourceDataRecord(String assetPublicKey,
                                                      //UUID id,
                                                      Resource resource) throws DatabaseOperationException, MissingAssetDataException
    {
        DatabaseTable databaseTable = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_TABLE_NAME);
        DatabaseTableRecord record = databaseTable.getEmptyRecord();

        record.setUUIDValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_ID_COLUMN, resource.getId());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_NAME_COLUMN, resource.getName());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_ASSET_PUBLIC_KEY_COLUMN, assetPublicKey);
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_FILE_NAME_COLUMN, resource.getFileName());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_RESOURCE_DENSITY_COLUMN, resource.getResourceDensity().getCode());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_RESOURCE_TYPE_COLUMN, resource.getResourceType().value());
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_PATH_COLUMN, resource.getResourceFile().getPath());

        return record;
    }

    private DatabaseTableRecord getContractDataRecord(String assetPublicKey,
                                                      //UUID id,
                                                      String name,
                                                      String value) throws DatabaseOperationException, MissingAssetDataException
    {
        DatabaseTable databaseTable = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_TABLE_NAME);
        DatabaseTableRecord record = databaseTable.getEmptyRecord();

        //record.setUUIDValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_ID_COLUMN, id);
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_ASSET_PUBLIC_KEY_COLUMN, assetPublicKey);
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_VALUE_COLUMN, value);
        record.setStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_NAME_COLUMN, name);

        return record;
    }

    private DatabaseTransaction addResourceRecordsToTransaction(DatabaseTransaction transaction, AssetFactory assetFactory) throws DatabaseOperationException, MissingAssetDataException, CantLoadTableToMemoryException
    {
        Resource resource = null;

        resource = assetFactory.getResource();
        if (resource != null)
        {
            DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_TABLE_NAME);

            DatabaseTableRecord resourceRecord = getResourceDataRecord(assetFactory.getPublicKey(), resource);
            DatabaseTableFilter filter = getResourceFilter(resource.getId().toString());

            if (isNewRecord(table, filter))
            {
                //New Records
                transaction.addRecordToInsert(table, resourceRecord);
            }
            else{
                ////update Records
                table.setStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
                transaction.addRecordToUpdate(table, resourceRecord);
            }

            if(assetFactory.getResources() != null)
            {
                //TODO: Verificar si debe haber una lista de Resource en la interfaz AseetFactory, para implementar dicha logica
            }



        }

        return transaction;
    }

    private DatabaseTransaction addResourceContractToTransaction(DatabaseTransaction transaction, AssetFactory assetFactory) throws DatabaseOperationException, MissingAssetDataException, CantLoadTableToMemoryException
    {
        //Contract contract = null;

        ContractProperty contractProperty = null;

        //contract = assetFactory.getContract();
        contractProperty = assetFactory.getContractProperty();
        if (contractProperty != null)
        {
            DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_TABLE_NAME);

            DatabaseTableRecord contractRecord = getContractDataRecord(assetFactory.getPublicKey(), contractProperty.getName(), contractProperty.getValue().toString());
            DatabaseTableFilter filter = getContractFilter(contractProperty.getName());

            if (isNewRecord(table, filter))
            {
                //New Records
                transaction.addRecordToInsert(table, contractRecord);
            }
            else{
                ////update Records
                table.setStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
                transaction.addRecordToUpdate(table, contractRecord);
            }

            if(assetFactory.getContractProperties() != null)
            {
                //TODO: Verificar si debe haber una lista de Contract en la interfaz AseetFactory, para implementar dicha logica
            }
        }

        return transaction;
    }

    private DatabaseTableFilter getContractFilter(String value)
    {
        DatabaseTableFilter filter = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_TABLE_NAME).getEmptyTableFilter();
        filter.setColumn(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_NAME_COLUMN);
        filter.setType(DatabaseFilterType.EQUAL);
        filter.setValue(value);

        return  filter;
    }

    private DatabaseTableFilter getResourceFilter(String value)
    {
        DatabaseTableFilter filter = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_TABLE_NAME).getEmptyTableFilter();
        filter.setColumn(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_ID_COLUMN);
        filter.setType(DatabaseFilterType.EQUAL);
        filter.setValue(value);

        return  filter;
    }

    private boolean isNewRecord(DatabaseTable table, DatabaseTableFilter filter) throws CantLoadTableToMemoryException {
        table.setStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
        table.loadToMemory();
        if (table.getRecords().isEmpty())
            return true;
        else
            return false;
    }

    private List<DatabaseTableRecord> getResourcesData(String assetFactoryPublicKey) throws CantLoadTableToMemoryException {
        DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_TABLE_NAME);

        table.setStringFilter(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_RESOURCE_ASSET_PUBLIC_KEY_COLUMN, assetFactoryPublicKey, DatabaseFilterType.EQUAL);
        table.loadToMemory();

        return table.getRecords();
    }

    private List<DatabaseTableRecord> getContractsData(String assetFactoryPublicKey) throws CantLoadTableToMemoryException {
        DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_TABLE_NAME);

        table.setStringFilter(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_ASSET_PUBLIC_KEY_COLUMN, assetFactoryPublicKey, DatabaseFilterType.EQUAL);
        table.loadToMemory();

        return table.getRecords();
    }

    private List<DatabaseTableRecord> getAssetFactoryData (DatabaseTableFilter filter) throws CantLoadTableToMemoryException
    {
        DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_TABLE_NAME);

        table.setStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
        table.loadToMemory();

        return table.getRecords();
    }

    //TODO: Metodo privado para instancia un objeto assetfactory vacio
    private AssetFactory getEmptyAssetFactory()
    {
        AssetFactory assetFactory = new AssetFactory() {
            String publicKey;
            String name;
            String description;
            Resource resource;
            List<Resource> resources;
            DigitalAssetContract digitalAssetContract;
            ContractProperty contractProperty;
            State state;
            List<ContractProperty> contractProperties;
            int quantity;
            long amount;
            long fee;
            WalletCategory walletCategory;
            WalletType walletType;
            Timestamp creationTimestamp;
            Timestamp lastModificationTimestamp;
            String walletPublicKey;
            String assetIssuerIdentityPublicKey;
            @Override
            public String getPublicKey() {
                return publicKey;
            }

            @Override
            public void setPublicKey(String publicKey) {
                this.publicKey = publicKey;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public Resource getResource() {
                return resource;
            }

            @Override
            public void setResource(Resource resource) {
                this.resource = resource;
            }

            @Override
            public List<Resource> getResources() {
                return resources;
            }

            @Override
            public void setResources(List<Resource> resources) {
                this.resources = resources;
            }

            @Override
            public DigitalAssetContract getContract() {
                return digitalAssetContract;
            }

            @Override
            public void setContract(DigitalAssetContract contract) {
                this.digitalAssetContract = contract;
            }

            @Override
            public ContractProperty getContractProperty() {
                return contractProperty;
            }

            @Override
            public void setContractProperty(ContractProperty contractProperty) {
                this.contractProperty = contractProperty;
            }

            @Override
            public List<ContractProperty> getContractProperties() {
                return contractProperties;
            }

            @Override
            public void setContractProperties(List<ContractProperty> contractProperties) {
                this.contractProperties = contractProperties;
            }

            @Override
            public State getState() {
                return state;
            }

            @Override
            public void setState(State state) {
                this.state = state;
            }

            @Override
            public int getQuantity() {
                return quantity;
            }

            @Override
            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            @Override
            public long getAmount() {
                return amount;
            }

            @Override
            public void setAmount(long amount) {
                this.amount = amount;
            }

            @Override
            public long getFee() {
                return fee;
            }

            @Override
            public void setFee(long fee) {
                this.fee = fee;
            }

            @Override
            public WalletCategory getWalletCategory() {
                return walletCategory;
            }

            @Override
            public void setWalletCategory(WalletCategory walletCategory) {
                this.walletCategory = walletCategory;
            }

            @Override
            public WalletType getWalletType() {
                return walletType;
            }

            @Override
            public void setWalletType(WalletType walletType) {
                this.walletType = walletType;
            }

            @Override
            public Timestamp getCreationTimestamp() {
                return creationTimestamp;
            }

            @Override
            public void setCreationTimestamp(Timestamp timestamp) {
                this.creationTimestamp = timestamp;
            }

            @Override
            public Timestamp getLastModificationTimestamp() {
                return lastModificationTimestamp;
            }

            @Override
            public void setLastModificationTimeststamp(Timestamp timestamp) {
                this.lastModificationTimestamp = timestamp;
            }

            @Override
            public String getWalletPublicKey() {
                return walletPublicKey;
            }

            @Override
            public void setWalletPublicKey(String walletPublicKey) {
                this.walletPublicKey = walletPublicKey;
            }

            @Override
            public String getAssetIssuerIdentityPublicKey() {
                return assetIssuerIdentityPublicKey;
            }

            @Override
            public void setAssetUserIdentityPublicKey(String assetUserIdentityPublicKey) {
                this.assetIssuerIdentityPublicKey = assetUserIdentityPublicKey;
            }
        };

       return assetFactory;
    }

    private AssetFactory getAssetFactory(DatabaseTableRecord assetFactoriesRecord) throws CantLoadTableToMemoryException, DatabaseOperationException, InvalidParameterException
    {
        AssetFactory assetFactory = getEmptyAssetFactory();

        assetFactory.setPublicKey(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_ASSET_PUBLIC_KEY_COLUMN));
        assetFactory.setName(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_NAME_COLUMN));
        assetFactory.setDescription(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_DESCRIPTION_COLUMN));
        assetFactory.setAmount(assetFactoriesRecord.getLongValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_AMOUNT_COLUMN));
        assetFactory.setAssetUserIdentityPublicKey(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_ISSUER_IDENTITY_PUBLIC_KEY_COLUMN));
        assetFactory.setFee(assetFactoriesRecord.getLongValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_FEE_COLUMN));
        assetFactory.setQuantity(assetFactoriesRecord.getIntegerValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_QUANTITY_COLUMN));
        assetFactory.setCreationTimestamp(Timestamp.valueOf(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CREATION_TIME_COLUMN)));
        assetFactory.setLastModificationTimeststamp(Timestamp.valueOf(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_LAST_UPDATE_TIME_COLUMN)));

        if (assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_TYPE_COLUMN) != null)
        {
            try {
                assetFactory.setWalletType(WalletType.getByCode(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_TYPE_COLUMN)));
            }
            catch (InvalidParameterException e)
            {
                assetFactory.setWalletType(WalletType.REFERENCE);
            }
        }

        if (assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_CATEGORY_COLUMN) != null)
        {
            try {
                assetFactory.setWalletCategory(WalletCategory.getByCode(assetFactoriesRecord.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_WALLET_CATEGORY_COLUMN)));
            }
            catch (InvalidParameterException e)
            {
                assetFactory.setWalletCategory(WalletCategory.REFERENCE_WALLET);
            }
        }


        return assetFactory;
    }

    public void saveAssetFactoryData(AssetFactory assetFactory) throws DatabaseOperationException, MissingAssetDataException
    {
        try {
            database = openDatabase();
            DatabaseTransaction transaction = database.newTransaction();

            DatabaseTable table = getDatabaseTable(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_TABLE_NAME);
            DatabaseTableRecord assetFactoryRecord = getAssetFactoryProjectRecord(assetFactory);
            DatabaseTableFilter filter = table.getEmptyTableFilter();
            filter.setType(DatabaseFilterType.EQUAL);
            filter.setValue(assetFactory.getPublicKey());
            filter.setColumn(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_ASSET_PUBLIC_KEY_COLUMN);

            if (isNewRecord(table, filter))
                transaction.addRecordToInsert(table, assetFactoryRecord);
            else {
                table.setStringFilter(filter.getColumn(), filter.getValue(), filter.getType());
                transaction.addRecordToUpdate(table, assetFactoryRecord);

                transaction = addResourceRecordsToTransaction(transaction, assetFactory);

                transaction = addResourceContractToTransaction(transaction, assetFactory);
            }

            //I execute the transaction and persist the database side of the asset.
            database.executeTransaction(transaction);
            database.closeDatabase();
        }catch (Exception e) {
            if (database != null)
                database.closeDatabase();
            throw new DatabaseOperationException(DatabaseOperationException.DEFAULT_MESSAGE, e, "Error trying to save the Asset Factory in the database.", null);
        }
    }

    public List<AssetFactory> getAssetFactoryList(DatabaseTableFilter filter) throws DatabaseOperationException {
        Database database= null;
        try {
            database = openDatabase();
            List<AssetFactory> assetFactoryList =  new ArrayList<>();

            // I will add the Asset Factory information from the database
            for (DatabaseTableRecord assetFactoriesRecord : getAssetFactoryData(filter)){

                AssetFactory assetFactory = getAssetFactory(assetFactoriesRecord);

                List<ContractProperty> contractProperties =  new ArrayList<>();
                // I will add the contract properties information from database
                for (DatabaseTableRecord contractpropertyRecords : getContractsData(assetFactory.getPublicKey())){
                    //TODO: Revisar este objeto contractProperty
                    ContractProperty contractProperty = new ContractProperty(null, null);

                    contractProperty.setName(contractpropertyRecords.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_NAME_COLUMN));
                    contractProperty.setValue(contractpropertyRecords.getStringValue(AssertFactoryMiddlewareDatabaseConstant.ASSET_FACTORY_CONTRACT_VALUE_COLUMN));

                    contractProperties.add(contractProperty);
                }

                // I will add the resource properties information from database

                assetFactory.setContractProperties(contractProperties);

                //TODO: Falta la tabla recursos

                assetFactoryList.add(assetFactory);
            }

            database.closeDatabase();

            return assetFactoryList;
        }catch (Exception e){
            if (database != null)
                database.closeDatabase();
            throw new DatabaseOperationException(DatabaseOperationException.DEFAULT_MESSAGE, e, "error trying to get projects from the database with filter: " + filter.toString(), null);
        }

    }

}