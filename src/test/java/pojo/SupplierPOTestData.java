package pojo;

import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import applicationController.BaseTest;

public class SupplierPOTestData extends BaseTest {

	protected String PO_NUMBER;
	protected String SUPPLIER_EMAIL;
	protected String BUYER_EMAIL;
	protected String path;
	protected String PASSWORD;
	protected String customerName;
	protected String FIRST_SUPPLIER_DATA;
	protected String SECOND_SUPPLIER_DATA;
	protected String CUSTOMER_DATA;
	protected String FUNDEREMAIL;
	protected String FUNDING_REQUEST_TYPE;
	
	public SupplierPOTestData(String path) {
		this.path = path;
	}

	public String readJson(String name) {

		String value = null;
		try {
			FileReader reader = new FileReader(path);
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			JsonObject questionnaireObject = jsonObject.getAsJsonObject("supplier_PO");
			value = questionnaireObject.get(name).getAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public String getPO_Number() {
		return PO_NUMBER = readJson("PO_Number");
	}

	public String getSupplierEmailID() {
		return SUPPLIER_EMAIL = readJson("supplierEmailAddress");
	}

	public String getBuyerEmailID() {
		return BUYER_EMAIL = readJson("buyerEmailAddress");
	}

	public String getPassword() {
		return PASSWORD = readJson("password");
	}

	public String getCustomerName() {
		return customerName = readJson("customerName");
	}

	public String getFirstSupplierData() {
		return FIRST_SUPPLIER_DATA = readJson("firstSupplierEmail");
	}

	public String getSecondSupplierData() {
		return SECOND_SUPPLIER_DATA = readJson("secondSupplierEmail");
	}

	public String getFunderEmailAddress() {
		return FUNDEREMAIL = readJson("fundermailAddress");
	}
	
	public String getFundingRequestType() {
		return FUNDING_REQUEST_TYPE = readJson("fundingRequestType");
	}
}


