package rokolabs.com.peoplefirst.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

	ResultReceiver receiver;

	public FetchAddressIntentService() {
		super("FetchAddressIntentService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		if (intent == null) return;

		receiver = intent.getParcelableExtra("rokolabs.com.peoplefirst.RECEIVER");
		String location = intent.getStringExtra("rokolabs.com.peoplefirst.LOCATION_DATA_EXTRA");

		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> geocoderAddresses;

		String message = "";
		ArrayList<String> resultAddressList = new ArrayList<>();

		try {
			geocoderAddresses = geocoder.getFromLocationName(location, 5);
		} catch (Exception e) {
			e.printStackTrace();
			message = "service not available";
			sendResult(message, resultAddressList);
			return;
		}

		if (geocoderAddresses.isEmpty()) {
			message = "no suggestions";
		} else {
			Log.d("FetchAddress", "found " + geocoderAddresses.size() + " addresses");
			for (Address address : geocoderAddresses) {
				ArrayList<String> addressFragments = new ArrayList<>();
				for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
					addressFragments.add(address.getAddressLine(i));
				}
				resultAddressList.add(TextUtils.join(System.getProperty("line.separator"), addressFragments));
			}
		}
		sendResult(message, resultAddressList);
	}

	private void sendResult(String message, ArrayList<String> resultAddressList) {
		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		bundle.putSerializable("addressList", resultAddressList);
		receiver.send(0, bundle);
	}
}
