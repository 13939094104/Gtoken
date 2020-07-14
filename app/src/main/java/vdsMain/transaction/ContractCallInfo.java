package vdsMain.transaction;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ContractCallInfo {

    public byte vm_version=4;

    public byte gas_price=40;

    public int gas_limit;

    private String functionName;

    private List<String> inputTypes;

    private List<Object> arguments;

    public String contract_address;

    private List<String> outputTypes;

    public ContractCallInfo(int gas_limit,String functionName,List<String> inputTypes,List<Object> arguments,List<String> outputTypes,String contract_address){
        this.gas_limit=gas_limit;
        this.functionName=functionName;
        this.inputTypes=inputTypes;
        this.arguments=arguments;
        this.outputTypes=outputTypes;
        this.contract_address=contract_address;
    }

    public int getGasLimitUsed(){
        return gas_limit*gas_price;
    }

    public String getDataHex(){
        Function function= null;
        try {
            function = FunctionEncoder.makeFunction(functionName,
                    inputTypes,arguments ,outputTypes);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        String encodedFunction=FunctionEncoder.encode(function);
        return encodedFunction.substring(2);
    }
}
