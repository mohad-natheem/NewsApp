package com.loc.newsapp.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.protobuf.Empty
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.home.components.ArticleCard

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles : List<Article>,
    onClick:(Article) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        contentPadding = PaddingValues(all = ExtraSmallPadding2)
    ){
        items(
            count = articles.size,
        )
        {
            val article = articles[it]
            ArticleCard(article = article, onClick = {onClick(article)})

        }
    }


}
@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles : LazyPagingItems<Article>,
    onClick:(Article) -> Unit
) {
    val handlePagingResult = HandlePagingResult(articles = articles)

    if (handlePagingResult){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ){
            items(
                count = articles.itemCount,
            )
                {
                    articles[it]?.let {article ->
                        ArticleCard(article = article, onClick = {onClick(article)})
                    }
                }
        }
    }

}

@Composable
fun HandlePagingResult(
    articles: LazyPagingItems<Article>,
) :Boolean{
    val loadState = articles.loadState
    val error = when{
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        else -> null
    }
    return when{
        loadState.refresh is LoadState.Loading ->{
            ShimmerEffect()
            false
        }
        error != null ->{
            EmptyScreen()
            false
        }
        else ->{
            true
        }
    }
    
}

@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1), modifier = Modifier.fillMaxSize().padding(horizontal = MediumPadding1)) {
        repeat(10){
            ArticleShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }

    }

}