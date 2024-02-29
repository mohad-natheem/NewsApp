package com.loc.newsapp.data.remote.dto

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.data.remote.NewsApi
import com.loc.newsapp.domain.model.Article

class SearchNewsPagingSource(
    private val api : NewsApi,
    private val searchQuery : String,
    private val sources : String
):PagingSource<Int,Article>(){
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
    return state.anchorPosition?.let { anchorPage ->
        val page = state.closestPageToPosition(anchorPage)
        page?.nextKey?.minus(1)?:page?.prevKey?.plus(1)
    }
    }
    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params?.key ?: 1
        return try{
            val newsResponses = api.searchNews(searchQuery = searchQuery,sources = sources,page = page)
            totalNewsCount += newsResponses.articles.size
            val articles = newsResponses.articles.distinctBy { it.title }

            LoadResult.Page(
                data = articles,
                nextKey = if(totalNewsCount == newsResponses.totalResults) null else page+1,
                prevKey = null
            )
        }catch (e : Exception){
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}