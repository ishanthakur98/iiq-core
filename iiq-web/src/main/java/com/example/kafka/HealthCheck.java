package com.example.kafka;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("/")
public class HealthCheck {

//    @Override
//    protected boolean isAuthorized(Authorizer... authorizers) throws GeneralException {
//        setPreAuth(true);
//
//        return true;
//    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response health(){
        System.out.println("inside health check");
        try {
            System.get
            URL uri = new URL("http://localhost:8080/iiq/login.jsf");
            HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if(code >= 200 && code < 300){
                return Response.ok("Ok").build();
            }

            return Response.status(503).entity("login.jsp code: " + code).build();
        }catch (Exception e){
            return Response.status(503).entity("login.jsp unreachable").build();
        }
    }
    public int[] searchRange(int[] nums, int target) {
        int firstOccur = floor(nums,target);
        int lastOccur = ceil(nums,target);
        return new int[]{firstOccur,lastOccur};
    }

    public int floor(int[] nums, int target){
        int start = 0;
        int end = nums.length;

        int firstOccur = -1;

        while(start <= end){
            int mid = start + (end - start)/2;
            if(nums[mid] == target){
                firstOccur = mid;
                end = mid - 1;
            }else if(target > nums[mid]){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return firstOccur;
    }

    public int ceil(int[] nums, int target){
        int start = 0;
        int end = nums.length;
        int lastOccur = -1;
        while(start <= end){
            int mid = start + (end - start)/2;
            if(nums[mid] == target){
                lastOccur = mid;
                start = mid + 1;
            }else if(target > nums[mid]){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        return lastOccur;
    }
}
