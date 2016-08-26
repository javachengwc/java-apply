namespace java com.thrift.finagle.result
namespace rb thrifts.result

struct FailDesc {
	1:string name,
	2:string failCode,
	3:string desc
}

struct Result {

	1:i32 code,

	2:optional list<FailDesc> failDescListT
}

struct StringResult {
	1:Result result,
	2:optional string value,
	3:optional string extend
}

service TransDealService{

    StringResult transDeal(1:i32 dealId);

    Result batchTransDeal(1:list<i32> dealIdList);
}