package net.sqindia.movehaul;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;


@SuppressWarnings("deprecation")

//asdff
public class HttpUtils {

    public static final String TAG = "tagH";


    public static String makeRequest(String url, String json) {
        Log.v(TAG, "URL-->" + url);
        Log.v(TAG, "input-->" + json);


        try {
            Log.v(TAG, "inside-->");



            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        }
        return "";
    }



    public static String makeRequest0(String url) {
        Log.v(TAG, "URL-->" + url);
       // Log.v(TAG, "input-->" + json);


        try {
            Log.v(TAG, "inside-->");



            HttpPost httpPost = new HttpPost(url);
            HttpGet httpGet = new HttpGet(url);
          //  httpPost.setEntity(new StringEntity(json));
           // httpPost.setHeader("Accept", "application/json");
           // httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag","er:"+e.toString());
        }
        return "";
    }





    public static String makeRequest1(String url, String json,String id,String token) {
        Log.v(TAG, "URL-->" + url);
        Log.v(TAG, "input-->" + json);


        try {
            Log.v(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("id",id);
            httpPost.setHeader("sessiontoken",token);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static JSONObject getData(String url,String id,String token) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("id",id);
            httppost.setHeader("sessiontoken",token);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;
            return jArray;

        }

        // Convert response to string

        if (is.equals(null)) {

            result = "sam";
            jArray = new JSONObject(result);
            return jArray;

        } else {


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("tag", "Error converting result " + e.toString());
                result = "sam";
            }

            try {

                jArray = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("tag", result);
                Log.e("tag", jArray.toString());
                Log.e("tag", "Error parsing data " + e.toString());


            }

            return jArray;
        }

    }


    public static JSONObject getCity(String url) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            //  HttpPost httppost = new HttpPost(url);
            // httpGet.setHeader("X-Originating-Ip", "103.48.181.209");
            httpGet.setHeader("Accept", "application/json");
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;
            return jArray;

        }

        // Convert response to string

        if (is.equals(null)) {

            result = "sam";
            jArray = new JSONObject(result);
            return jArray;

        } else {


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("tag", "Error converting result " + e.toString());
                result = "sam";
            }

            try {

                jArray = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("tag", result);
                Log.e("tag", jArray.toString());
                Log.e("tag", "Error parsing data " + e.toString());


            }

            return jArray;
        }

    }


    public static JSONObject logout(String url, String session) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        Log.e("tag_", "started");
        try {

            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("session_id", session);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            String text;
            try {
                response = client.execute(httppost);
                Log.e("tag_", "stsL_" + response.getStatusLine());
                Log.e("tag_", "stsL_" + response.getStatusLine().getReasonPhrase());
                Log.e("tag_", "stsL_" + response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                Log.e("INFO", e.getMessage());
            }


            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("tag_", "stsL2_" + is);

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());

        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("tag_", "stsL20ifaft_" + result);
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
            // result = "sam";
        }

        try {
            jArray = new JSONObject(result);
            Log.e("tag_", "stsL21if_" + jArray);
        } catch (JSONException e) {
            Log.e("tag0", result);
            Log.e("tag2", "Error parsing data " + e.toString());
        }

        return jArray;
        // return jArray;
        // }

    }










    public static JSONObject getData2(String url, String country) {
        InputStream is = null;
        String json, result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("country", country);
            json = jsonObject.toString();


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("apikey", "1eo7u4tig9704k2humvdywwnb4hnl2xa1jbrh7go");
            httppost.setEntity(new StringEntity(json));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
        }

        try {

            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("tag", "Error parsing data " + e.toString());
        }

        return jArray;
    }


    public static JSONObject getData3(String url) {
        InputStream is = null;
        String json, result = "";
        JSONObject jArray = null;
        // Download JSON data from URL
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Accept", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
        }

        try {

            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("tag", "Error parsing data " + e.toString());
        }

        return jArray;
    }


    public static String makeRequest2(String url, String json, String session) {
        Log.v(TAG, "URL-->" + url);
        Log.v(TAG, "input-->" + json);


        try {
            Log.v(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("session_id", session);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static JSONObject getVirtual(String url, String session) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("session_id", session);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;
            return jArray;

        }

        // Convert response to string

        if (is.equals(null)) {

            result = "sam";
            jArray = new JSONObject(result);
            return jArray;

        } else {


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("tag", "Error converting result " + e.toString());
                result = "sam";
            }

            try {

                jArray = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("tag", result);
                Log.e("tag", jArray.toString());
                Log.e("tag", "Error parsing data " + e.toString());


            }

            return jArray;
        }

    }






    public static JSONObject getAllPal(String url, String data, String session) {
        InputStream is = null;
        String json, result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            Log.d("tag",""+url+data+session);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("keyword", data);
            json = jsonObject.toString();


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("session_id",session);
            httppost.setEntity(new StringEntity(json));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("tag", "Error converting result " + e.toString());
        }

        try {

            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("tag", "Error parsing data " + e.toString());
        }

        return jArray;
    }




    public static String makeRequest34(String url, String json, String session) {

        Log.e(TAG, "URL-->" + url);
        Log.e(TAG, "input-->" + json);


        try {
            Log.e(TAG, "inside-->");

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("session_id",session);


            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);


            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.e(TAG, "output-->" + result);
                return result;
            } else {
                Log.e(TAG, "output-->" + inputStream);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }




















    public static JSONObject getPalLists(String url, String session) throws JSONException {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("session_id",session);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("tag", "Error in http connection " + e.toString());
            result = "sam";
            is = null;
            return jArray;

        }

        // Convert response to string

        if (is.equals(null)) {

            result = "sam";
            jArray = new JSONObject(result);
            return jArray;

        } else {


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("tag", "Error converting result " + e.toString());
                result = "sam";
            }

            try {

                jArray = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("tag", result);
                Log.e("tag", jArray.toString());
                Log.e("tag", "Error parsing data " + e.toString());


            }

            return jArray;
        }

    }





















    public static String makeRequest1(String url, String json, String token) {
        Log.v(TAG, "URL-->" + url);
        Log.v(TAG, "input-->" + json);
        Log.d("tag", token);


        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("sessionToken", token);
            //text/html
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);

            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                String result = convertInputStreamToString(inputStream);
                Log.v(TAG, "output-->" + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Header adding multiple parameter>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        System.out.println(" OUTPUT -->" + result);


        return result;

    }
}
