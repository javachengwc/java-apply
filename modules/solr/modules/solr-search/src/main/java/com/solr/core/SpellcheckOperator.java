package com.solr.core;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;

import java.util.List;
import java.util.Map;

/**
 * 处理拼写检查的solr查询请求
 */
public class SpellcheckOperator {

	private SolrServer solrServer;
	private SolrQuery params;

	public SpellcheckOperator(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public SpellCheckResponse getSpellCheckResult(String token) {
		//构造查询参数
		buildSpellQueryParams(token);
		QueryResponse response = null;
		SpellCheckResponse spellCheckResponse=null;
		  
        try {  
            response = solrServer.query(params);
            spellCheckResponse = response.getSpellCheckResponse();
        } catch (SolrServerException e) {  
            System.err.println(e.getMessage());  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.err.println(e.getMessage());  
            e.printStackTrace();  
        }
        return spellCheckResponse;
	}
	public QueryResponse getQueryResponse(String token) {
		//构造查询参数
		buildQueryParams(token);
		QueryResponse response = null;
		
		try {  
            response = solrServer.query(params);
         } catch (SolrServerException e) {  
            System.err.println(e.getMessage());  
            e.printStackTrace();  
        } catch (Exception e) {  
            System.err.println(e.getMessage());  
            e.printStackTrace();  
        }
        return response;
	}

	private void dealSpellCheckResponse(String token, QueryResponse response) {
		SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();  
		if (spellCheckResponse != null) {  
		    List<Suggestion> suggestionList = spellCheckResponse.getSuggestions();  
		    for (Suggestion suggestion : suggestionList) {  
		        System.out.println("Suggestions NumFound: " + suggestion.getNumFound());  
		        System.out.println("Token: " + suggestion.getToken());  
		        System.out.print("Suggested: ");  
		        List<String> suggestedWordList = suggestion.getAlternatives();  
		        for (String word : suggestedWordList) {  
		            System.out.println(word + ", ");  
		        }  
		        System.out.println();  
		    }  
		    System.out.println();  
		    Map<String, Suggestion> suggestedMap = spellCheckResponse.getSuggestionMap();  
		    for (Map.Entry<String, Suggestion> entry : suggestedMap.entrySet()) {  
		        System.out.println("suggestionName: " + entry.getKey());  
		        Suggestion suggestion = entry.getValue();  
		        System.out.println("NumFound: " + suggestion.getNumFound());  
		        System.out.println("Token: " + suggestion.getToken());  
		        System.out.print("suggested: ");  
     
		        List<String> suggestedList = suggestion.getAlternatives();  
		        for (String suggestedWord : suggestedList) {  
		            System.out.print(suggestedWord + ", ");  
		        }  
		        System.out.println("\n\n");  
		    }  
     
		    Suggestion suggestion = spellCheckResponse.getSuggestion(token);  
		    System.out.println("NumFound: " + suggestion.getNumFound());  
		    System.out.println("Token: " + suggestion.getToken());  
		    System.out.print("suggested: ");  
		    List<String> suggestedList = suggestion.getAlternatives();  
		    for (String suggestedWord : suggestedList) {  
		        System.out.print(suggestedWord + ", ");  
		    }  
		    System.out.println("\n\n");  
     
		    System.out.println("The First suggested word for solr is : " + spellCheckResponse.getFirstSuggestion(token));  
		    System.out.println("\n\n");  
     
		    List<Collation> collatedList = spellCheckResponse.getCollatedResults();  
		    if (collatedList != null) {  
		        for (Collation collation : collatedList) {  
		            System.out.println("collated query String: " + collation.getCollationQueryString());  
		            System.out.println("collation Num: " + collation.getNumberOfHits());  
		            List<Correction> correctionList = collation.getMisspellingsAndCorrections();  
		            for (Correction correction : correctionList) {  
		                System.out.println("original: " + correction.getOriginal());  
		                System.out.println("correction: " + correction.getCorrection());  
		            }  
		            System.out.println();  
		        }  
		    }  
		    System.out.println();  
		    System.out.println("The Collated word: " + spellCheckResponse.getCollatedResult());  
		    System.out.println();  
		}
	}
	private void buildQueryParams(String token) {
		params = new SolrQuery();
		params.set("q", token);
		params.setSortField("total_search_counts", ORDER.desc);
		params.setRows(10);
	
	}

	private void buildSpellQueryParams(String token) {
		params = new SolrQuery();
		params.set("qt", "/spell");
		params.set("q", token);
		params.set("spellcheck", "on");
		params.set("spellcheck.build", "true");
		params.set("spellcheck.onlyMorePopular", "true");

		params.set("spellcheck.count", "10");
		//params.set("spellcheck.alternativeTermCount", "4");
		params.set("spellcheck.onlyMorePopular", "true");

//		params.set("spellcheck.extendedResults", "true");
//		params.set("spellcheck.maxResultsForSuggest", "10");
//
		params.set("spellcheck.collate", "true");
		params.set("spellcheck.collateExtendedResults", "true");
		params.set("spellcheck.maxCollationTries", "5");
		params.set("spellcheck.maxCollations", "3");
	}

}
