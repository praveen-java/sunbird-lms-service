/**
 * 
 */
package controllers.coursemanagement;

import akka.util.Timeout;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.BaseController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.sunbird.common.models.util.ActorOperations;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.HeaderParam;
import org.sunbird.common.request.Request;
import org.sunbird.common.request.RequestValidator;

import play.libs.F.Promise;
import play.mvc.Result;

/**
 * This controller will handle all the API related to course batches , add batch,update batch,join member to batch,
 * remove member from batch, get particular batch details.
 * 
 * @author Manzarul
 */
public class CourseBatchController extends BaseController {

  /**
   * This method will add a new batch for a particular course.
   * 
   * @return Promise<Result>
   */
  public Promise<Result> createBatch() {

    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("create new batch request data=" + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateCreateBatchReq(reqObj);
      reqObj.setOperation(ActorOperations.CREATE_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      innerMap.put(JsonKey.HEADER, getAllRequestHeaders(request()));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  /**
   * This method will update existing batch details.
   * @return Promise<Result>
   */
  public Promise<Result> updateBatch() {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("update batch request data=" + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateUpdateCourseBatchReq(reqObj);
      reqObj.setOperation(ActorOperations.UPDATE_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

 
  /**
   * This method will do soft delete to the batch.
   * @return Promise<Result>
   */
  public Promise<Result> deleteBatch() {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("Delete batch=" + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateAddBatchCourse(reqObj);
      reqObj.setOperation(ActorOperations.REMOVE_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      Promise<Result> res =
          actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
      return res;
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  
  
  /**
   * This method will do the user batch enrollment 
   * @return Promise<Result>
   */
  public Promise<Result> addUserToBatch(String batchId) {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("Add user to batch=" + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      reqObj.setOperation(ActorOperations.ADD_USER_TO_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      reqObj.getRequest().put(JsonKey.BATCH_ID ,batchId);
      HashMap<String, Object> innerMap = new HashMap<>();
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      RequestValidator.validateAddBatchCourse(reqObj);
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      System.out.println("CALING BBATCH ACTOR FROM CONTROLLER");
      Promise<Result> res =
          actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
      return res;
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  
  /**
   * This method will remove user batch enrollment.
   * @return Promise<Result>
   */
  public Promise<Result> removeUsersFromBatch() {
    try {
      JsonNode requestData = request().body().asJson();
      ProjectLogger.log("Remove user to batch=" + requestData, LoggerEnum.INFO.name());
      Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
      RequestValidator.validateAddBatchCourse(reqObj);
      reqObj.setOperation(ActorOperations.REMOVE_USER_FROM_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      Promise<Result> res =
          actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
      return res;
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  
  /**
   * This method will remove user batch enrollment.
   * @return Promise<Result>
   */
  public Promise<Result> getBatch(String batchId) {
    try {
      ProjectLogger.log("get batch=" +batchId, LoggerEnum.INFO.name());
      Request reqObj = new Request();
      reqObj.setOperation(ActorOperations.GET_BATCH.getValue());
      reqObj.setRequest_id(ExecutionContext.getRequestId());
      reqObj.setEnv(getEnvironment());
      HashMap<String, Object> innerMap = new HashMap<>();
      reqObj.getRequest().put(JsonKey.BATCH_ID, batchId);
      innerMap.put(JsonKey.BATCH, reqObj.getRequest());
      innerMap.put(JsonKey.REQUESTED_BY,
          getUserIdByAuthToken(request().getHeader(HeaderParam.X_Authenticated_Userid.getName())));
      reqObj.setRequest(innerMap);
      Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
      return actorResponseHandler(getRemoteActor(), reqObj, timeout, null, request());
    } catch (Exception e) {
      return Promise.<Result>pure(createCommonExceptionResponse(e, request()));
    }
  }

  
  /**
   * 
   * @param request
   * @return Map<String, String>
   */
  private Map<String, String> getAllRequestHeaders(play.mvc.Http.Request request) {
    Map<String, String> map = new HashMap<>();
    Map<String, String[]> headers = request.headers();
    Iterator<Entry<String, String[]>> itr = headers.entrySet().iterator();
    while (itr.hasNext()) {
      Entry<String, String[]> entry = itr.next();
      map.put(entry.getKey(), entry.getValue()[0]);
    }
    return map;
  }

  
  }