import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DNS {

	public static void main(String[] args) {
		DNS dns=new DNS();
		List<Integer> iplist = dns.getIPsFromPayloadString("652E81800001000200020002036D703306676F75676F7503636F6D0000010001C00C000100010000069200043AFB39D1C00C000100010000069200043AFB39D2C01000020001000002B50006036E7331C010C01000020001000002B50006036E7332C010C04C00010001000002B500043AFB3968C05E00010001000003D700043A3D27DE");
		Iterator<Integer> it = iplist.iterator();
		while(it.hasNext()){
			System.out.println(longToIP(it.next()));
		}
	}
	
	//将整数形式转换成127.0.0.1字符串形式的ip地址    
    public static String longToIP(long longIp) {    
        StringBuffer sb = new StringBuffer("");    
        sb.append(String.valueOf((longIp >>> 24)));    
        sb.append(".");    
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));    
        sb.append(".");    
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));    
        sb.append(".");    
        sb.append(String.valueOf((longIp & 0x000000FF)));    
        return sb.toString();    
    }    

	
	public int hexToInt8(String str){
		int result = 0;
		for(int i=0;i<2;i++){
			result <<= 4;
			result += "0123456789ABCDEF".indexOf(str.charAt(i));
		}
		return result;
	}

	public int hexToInt16(String str){
		int result = 0;
		for(int i=0;i<4;i++){
			result <<= 4;
			result += "0123456789ABCDEF".indexOf(str.charAt(i));
		}
		return result;
	}
	
	public int hexToInt32(String str){
		int result = 0;
		for(int i=0;i<8;i++){
			result <<= 4;
			result += "0123456789ABCDEF".indexOf(str.charAt(i));
		}
		return result;
	}
	
	public List<Integer> getIPsFromPayloadString(String payload){
		List<Integer> IPList = new ArrayList<Integer>();
		int offset = 0;
		int cc=0;
		int num=0;
		int id = hexToInt16(payload.substring(offset));
		offset+=4;
		//System.out.println("id: " + Integer.toHexString(id));
		int op = hexToInt16(payload.substring(offset));
		offset+=4;
		//System.out.println("op: " + Integer.toHexString(op));
		int qc = hexToInt16(payload.substring(offset));
		offset+=4;
		//System.out.println("qc: " + Integer.toHexString(qc));
		int ac = hexToInt16(payload.substring(offset));
		offset+=4;
		//System.out.println("ac: " + Integer.toHexString(ac));
		//
		offset+=8;
		//question
		cc = qc;
		do{
			//name
			while((num=hexToInt8(payload.substring(offset)))!=0){
				if((num&0xc0)!=0){
					offset+=4;
					break;
				}else{
					offset+=2;
					offset+=num*2;
				}
			}
			if(num==0){
				offset += 2;
			}
			//type
			//int itype=HexToInt16(payload.substring(offset));
			offset+=4;
			//class
			//int iclass=HexToInt16(payload.substring(offset));
			offset+=4;
		}while(--cc>0);
		//anwer
		cc=ac;
		do{
			//name
			while((num=hexToInt8(payload.substring(offset)))!=0){
				if((num&0xc0)!=0){
					offset+=4;
					break;
				}else{
					offset+=2;
					offset+=num*2;
				}
			}
			if(num==0){
				offset += 2;
			}
			//type
			int itype=hexToInt16(payload.substring(offset));
			offset+=4;
			//class
			int iclass=hexToInt16(payload.substring(offset));
			offset+=4;
			//ttl
			offset+=8;
			//rdlength
			int ll=hexToInt16(payload.substring(offset));
			offset+=4;
			//rddata
			if(itype==1 && iclass==1 && ll==4){
				//System.out.println(HexToInt32(payload.substring(offset)));
				IPList.add(hexToInt32(payload.substring(offset)));
			}
			offset+=ll*2;
		}while(--cc>0);
		
		//authority
		//additional
		
		return IPList;
	}
}
