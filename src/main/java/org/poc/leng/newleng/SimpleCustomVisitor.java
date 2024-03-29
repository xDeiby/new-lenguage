package org.poc.leng.newleng;

import java.util.HashMap;
import java.util.Map;

public class SimpleCustomVisitor extends SimpleBaseVisitor<Object>{

	protected Map<String, String> _vars = new HashMap<String, String>();

	public SimpleCustomVisitor() {}
//
	@Override
    public Object visitStart_block(SimpleParser.Start_blockContext ctx) { 
	    	if(ctx.START().getText().equals("inicio"))    {
	    		System.out.println("#include <stdio.h>");
			System.out.println("");
			System.out.println("int main(void) {");
		}
	    return null;
	}
//	
	@Override
	public Object visitEnd_block(SimpleParser.End_blockContext ctx) {
	    	if(ctx.END().getText().equals("fin"))    {
	    		System.out.println("\treturn 0;");
			System.out.println("}");
		}
	    return null;
	}
//	
	@Override
	public Object visitDeclaration(SimpleParser.DeclarationContext ctx) {
		String var_type = ctx.type_date().getText();
        String id = ctx.ID().getText();
        if (!_vars.containsKey(id)) {
        		var_type = getVarType(var_type);
        		_vars.put(id, var_type);
        		        		
	        	System.out.println(String.format("\t%s %s;", var_type, id));
                
        } else {
        		throw new IllegalArgumentException("Variable '" + id + "' ya existe!!");
        }
        
        
		return null;
	}
	
	@Override
	public Object visitAssignation(SimpleParser.AssignationContext ctx) {
		String id = ctx.ID().getText();
		String date = ctx.operation().getText();
		
		System.out.println(String.format("\t%s = %s;", id,date));
		return null;
	}
	

	@Override
	public Object visitOp_read(SimpleParser.Op_readContext ctx) {
		String id = ctx.ID().getText();
		String format = "%s";
		System.out.println(String.format("\tscanf('%s', &%s);",format ,id));
		return null; 
	}
	
	@Override
	public Object visitOp_print(SimpleParser.Op_printContext ctx) {
		String expresion = ctx.operation().getText();
		String format = "%s";
		expresion = replace(expresion);
		
		System.out.println(String.format("\tprintf('%s ', %s);",format, expresion));
		
		return null;  
	}
	
	@Override
	public Object visitStart_if(SimpleParser.Start_ifContext ctx) {
		String conditions = ctx.condition().getText();
		conditions = replace(conditions);
		String type_if = ctx.IF_CON().getText();
		
		if(type_if.equals("si")) {
			type_if = "if";
		}
		else {
			type_if = "else if";
		}
		System.out.println(String.format("\t%s %s {",type_if, conditions));
		return null; 
	}
	
	@Override
	public Object visitStart_else(SimpleParser.Start_elseContext ctx) {
		System.out.println("\telse {");
		
		return null;
	}
	
	@Override
	public Object visitEnd_if(SimpleParser.End_ifContext ctx) {
		if(ctx.RKEY().getText().equals("}")) {
			System.out.println("\t}");
		}
		
		return null;
	}

	@Override
	public Object visitStart_wh(SimpleParser.Start_whContext ctx) {
		String conditions = ctx.condition().getText();
		conditions = replace(conditions);
		if(ctx.WHILE().getText().contentEquals("mientras")) {
			
			System.out.println(String.format("\twhile %s {", conditions));
		}
		return null;
	}


	@Override
	public Object visitEnd_wh(SimpleParser.End_whContext ctx) {
		System.out.println("\t}");
		
		return null;
	}
	
	@Override
	public Object visitStart_for(SimpleParser.Start_forContext ctx) {
		String range = ctx.rango().getText();
		range = replace(range);
		
		System.out.println(String.format("\tfor %s{",range));
		
		return null;
	}
	
	
	public Object visitEnd_for(SimpleParser.End_forContext ctx) {
		System.out.println("\t}");
		
		return null;
	}
	
	
	private String getVarType(String var_type) {
		if(var_type.equals("entero") || var_type.equals("logico"))
			return "int";
		else if(var_type.equals("real"))
			return "float";
		else
			return "char";
	}
//	
	private String getVarTypeMode(String var_type) {
		if(var_type.equals("int"))
			return "%d";
		else if(var_type.equals("real"))
			return "%f";
		else
			return "%s";
	}
//	
	private String replace(String stat) {
		stat = stat.replaceAll("=", "==");
		stat = stat.replaceAll("<>", "!=");
		stat = stat.replaceAll("and", "&&");
		stat = stat.replaceAll("or", "||");
		stat = stat.replaceAll(",", ";");
		stat = stat.replaceAll("<-", "=");

		
		return stat;
	}
//	
	
}
