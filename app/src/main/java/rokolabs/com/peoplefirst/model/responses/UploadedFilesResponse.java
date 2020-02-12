package rokolabs.com.peoplefirst.model.responses;

import java.util.ArrayList;

public class UploadedFilesResponse extends BaseResponse {

	public ArrayList<Data> data;

	public class Data {
		public String url;
	}
}
