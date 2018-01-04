package requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Payloads {


    public static HashMap createUserPayload() {

        Random r = new Random();
        int low = 10;
        int high = 100000;
        int randomNumber = r.nextInt(high-low) + low;

        HashMap<String, String> createUserPayload = new HashMap<String, String>();
        createUserPayload.put("email", String.format("withrating5A+%s@zonky.cz", randomNumber));
        createUserPayload.put("firstName", "Milan");
        createUserPayload.put("lastName", "Vojnovič");
        createUserPayload.put("password", "Zebra2014");
        return createUserPayload;
    }

    public static HashMap createApplication() {
        HashMap<String, Object> createApplicationPayload = new HashMap<String, Object>();
        createApplicationPayload.put("currentStep", 3);
        createApplicationPayload.put("purpose", "1");
        createApplicationPayload.put("requestedAmount", 100000);
        createApplicationPayload.put("requestedAnnuity", 1000);
        createApplicationPayload.put("applicationVersion", "5.1");
        createApplicationPayload.put("personalInfoConsent", true);
        createApplicationPayload.put("businessTermsConsent", true);
        createApplicationPayload.put("channelCode", "MOBILE_APP");
        createApplicationPayload.put("firstName", "Milan");
        createApplicationPayload.put("surname", "Vojnovič");

        return createApplicationPayload;
    }

    public static HashMap updateApplication()
    {
        HashMap<String, Object> updateApplicationPayload = new HashMap<String, Object>();
        HashMap<String, String> permanentAddress = new HashMap<String, String>();

        HashMap<String, Object> income = new HashMap<String, Object>();
        income.put("type", "1");
        income.put("primary", true);
        income.put("amount", 120000);
        income.put("employeeName", "Zonky s.r.o.");

        ArrayList<HashMap<String, Object>> incomes = new ArrayList<HashMap<String, Object>>();
        incomes.add(income);

        permanentAddress.put("street", "Ulice");
        permanentAddress.put("streetNo", "1");
        permanentAddress.put("city", "Praha");
        permanentAddress.put("zipCode", "10000");
        permanentAddress.put("country", "CZ");

        updateApplicationPayload.put("currentStep", 13);
        updateApplicationPayload.put("purpose", "4");
        updateApplicationPayload.put("requestedAmount", 123550);
        updateApplicationPayload.put("requestedAnnuity", 1235);
        updateApplicationPayload.put("applicationVersion", "5");
        updateApplicationPayload.put("personalInfoConsent", true);
        updateApplicationPayload.put("businessTermsConsent", true);
        updateApplicationPayload.put("personalNo", "8912247938");
        updateApplicationPayload.put("phone", "+420777888999");
        updateApplicationPayload.put("politicallyExposed", true);
        updateApplicationPayload.put("maritalStatus", "1");
        updateApplicationPayload.put("mainIncomeType",  "1");
        updateApplicationPayload.put("education", "6");
        updateApplicationPayload.put("housingType", "5");
        updateApplicationPayload.put("aboutMyself", "me want money");
        updateApplicationPayload.put("permanentAddress", permanentAddress);
        updateApplicationPayload.put("incomes", incomes);

        updateApplicationPayload.put("totalHouseholdIncome", 100000);
        updateApplicationPayload.put("totalExpenses", 15000);

        return updateApplicationPayload;
    }

}
