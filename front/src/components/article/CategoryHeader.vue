<template>
      <div class="content d-flex flex-column">
          <keep-alive>
            <CategoryIntro />
          </keep-alive>
          <div  class="d-flex justify-content-between mt-4">
              <div class="d-flex">
                  <div v-if="isNotice" class="d-flex">
                      <el-button type="primary" disabled>
                          <el-icon style="vertical-align: middle">
                              <EditPen />
                          </el-icon>
                          <span style="vertical-align: middle"> 글쓰기 </span>
                      </el-button>
                  </div>
                  <div v-else class="d-flex">
                      <el-button @click="replacePath('/'+route.name+'/new')" type="primary">
                          <el-icon style="vertical-align: middle">
                              <EditPen />
                          </el-icon>
                          <span style="vertical-align: middle"> 글쓰기 </span>
                      </el-button>
                  </div>
                  <div class="reload-button">
                      <el-button plain @click="onGetArticles()" style="border: none;">
                          <el-icon style="vertical-align: middle">
                              <Refresh />
                          </el-icon>
                      </el-button>
                  </div>
                  <div v-if="isAdmin()" class="create-dummies-button">
                      <el-button plain @click="onCreateDummies()&onGetArticles()" style="border: none;">
                          <el-icon style="vertical-align: middle">
                              <Plus />
                          </el-icon>
                      </el-button>
                  </div>
              </div>
              <div style="width: 50%;">
                  <el-input
                          v-model="search.keyword"
                          placeholder="Please input"
                          class="input-with-select"
                          @keyup.enter="onSearch()&onGetArticles()"
                    ><template #prepend>
                          <el-select class="select-search" v-model="search.condition" suffix-icon="">
                              <el-option v-for="item in selectList.search" :label="item.text" :value="item.value" />
                          </el-select>
                      </template>
                      <template #append>
                          <el-button plain @click="onSearch()&onGetArticles()" style="border: none;">
                              <el-icon style="vertical-align: middle">
                                  <Search />
                              </el-icon>
                          </el-button>
                      </template>
                  </el-input>
              </div>
              <div class="sort-options">
                  <el-select class="el-select-custom" v-model="sort.period" suffix-icon="" @change="onPeriod()&onGetArticles()">
                      <el-option class="options" v-for="item in selectList.period" :label="item.text" :value="item.value" />
                  </el-select>
                  <el-select class="el-select-custom" v-model="sort.sort" suffix-icon="" @change="onSort()&onGetArticles()">
                      <el-option class="options" v-for="item in selectList.sort" :label="item.text" :value="item.value" />
                  </el-select>
              </div>
          </div>
          <el-divider/>
          <keep-alive>
            <Articles />
          </keep-alive>
          <keep-alive>
            <Pagination  :key="route.fullPath"/>
          </keep-alive>
      </div>
</template>

<script setup lang="ts">

import {onMounted, provide, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import Articles from "@/components/article/Articles.vue";
import {createDummies, getArticles} from "@/controller/api/article";
import {useResponseStore} from "@/stores/Response";
import {useAuthStore} from "@/stores/auth";
import {useArticlesStore} from "@/stores/articles";
import type {Article} from "@/components/custom-types/article";
import Pagination from "@/components/article/Pagination.vue";
import CategoryIntro from "@/components/article/CategoryIntro.vue";
import type {Intro} from "@/components/custom-types/intro";
import {usePageStore} from "@/stores/page";

const route = useRoute();
const router = useRouter();
const resStore = useResponseStore();
const articlesStore = useArticlesStore();
const pageStore = usePageStore();

const isNotice = ref(false);

const sort = ref({
    period:'ALL',
    sort:'LATEST',
    isSetPeriod: false,
    isSetSort: false,
})

const search = ref({
    condition:'ALL',
    keyword:'',
    isSearch: false,
})

const isSetPage = ref(false);

const selectList = reactive({
    search: [
        {
            text: "제목+글",
            value: "ALL",
        },
        {
            text: "제목",
            value: "TITLE",
        },
        {
            text: "작성자",
            value: "AUTHOR",
        }
    ],
    period: [
        {
            text: "ALL",
            value: "ALL",
        },
        {
            text: "1D",
            value: "DAY",
        },
        {
            text: "1W",
            value: "WEEK",
        },
        {
            text: "1M",
            value: "MONTH",
        },
        {
            text: "1Y",
            value: "YEAR",
        },
    ],
    sort: [
        {
            text: "최신순",
            value: "LATEST",
        },
        {
            text: "조회순",
            value: "VIEW",
        },
        {
            text: "인기순",
            value: "LIKE",
        }
    ],
})

const onSearch = function () {
    search.value.isSearch = search.value.keyword !== '';
}

const onSort = function (){
    sort.value.isSetSort = true;
}

const onPeriod = function(){
    sort.value.isSetPeriod = true;
}
const onPaging = async function(page: number){
    await pageStore.setCurrentPage(page);
    isSetPage.value = true;
    await onGetArticles();
}


const isAdmin = function(){
    return useAuthStore().isAdmin;
}

onMounted(async ()=>{
    await setDefaultFromPath(route.fullPath);
    await onGetArticles();
    if(route.name === "notice"){
        isNotice.value = true;
    }
})

const replacePath = function (path: string) {
    router.replace(path);
}

function setDefaultFromPath(path: string):void {
    const regex = /[?&]([^=]+)=([^&]*)/g;
    let match: RegExpExecArray | null;

    pageStore.setCurrentPage(1);

    while ((match = regex.exec(path)) !== null) {
        const paramName = match[1];
        const paramValue = decodeURIComponent(match[2]);

        switch (paramName) {
            case "page":
                pageStore.setCurrentPage(parseInt(paramValue, 10));
                isSetPage.value = true;
                break;
            case "period":
                sort.value.period = paramValue;
                sort.value.isSetPeriod = true;
                break;
            case "sort":
                sort.value.sort = paramValue;
                sort.value.isSetSort = true;
                break;
            case "condition":
                search.value.condition = paramValue;
                break;
            case "keyword":
                search.value.keyword = paramValue;
                search.value.isSearch = true;
                break;
            default:
                break;
        }
    }
}

const setIntro = function(category: any) {
    let intro: Intro;
    switch(category){
        case "community":
            intro={
                category: "커뮤니티",
                message: "즐겁게 일상을 나누는 공간입니다.",
            }
            articlesStore.setIntro(intro);
            break;
        case "question":
            intro={
                category: "Q&A",
                message: "좋은 질문과 답변으로 함께 성장하는 공간입니다.",
            }
            articlesStore.setIntro(intro);
            break;
        case "information":
            intro={
                category: "정보공유",
                message: "정보를 공유하고 새로운 아이디어를 얻어보세요.",
            }
            articlesStore.setIntro(intro);
            break;
        case "notice":
            intro={
                category: "공지사항",
                message: "새로운 소식과 공지사항을 전해드리는 공간입니다.",
            }
            articlesStore.setIntro(intro);
            break;
        default:
            break;
    }
}

const getPath = function(){
    let isFirst = true;
    let path = '/';
    const checkChanges: any = ref([])

    if(typeof route.name === 'string'){
        path = path+route.name;
    }

    if(isSetPage.value) {
        checkChanges.value.push('page='+pageStore.getCurrentPage)
    }
    if(sort.value.isSetPeriod) {
        checkChanges.value.push('period='+sort.value.period)
    }
    if(sort.value.isSetSort) {
        checkChanges.value.push('sort='+sort.value.sort)
    }
    if(search.value.isSearch) {
        checkChanges.value.push('condition='+search.value.condition+'&keyword='+search.value.keyword)
    }
    checkChanges.value.forEach((item: any) => {
        if(item) {
            if (isFirst) {
                path = path + '?' + item
                isFirst = false
            } else {
                path = path + '&' + item
            }
        }
    });

    return path;
}

const onGetArticles = async function() {
    await getArticles(getPath())
        .then((response: any)=>{
            if(resStore.isOK){
                articlesStore.clean();
                response.articleList.forEach((r: any) => {
                    const article: Article = {
                        id: r.id,
                        title: r.title,
                        createdAt: r.createdAt,
                        viewNum: r.viewNum,
                        commentNum: r.commentNum,
                        likeNum: r.likeNum,
                        authorId: r.authorId,
                        nickname: r.nickname,
                        imagePath: r.imagePath,
                    }
                    articlesStore.addArticle(article);
                });
                pageStore.setMaxPage(response.maxPage);
                setIntro(route.name);
                router.replace(getPath());
                scrollTo(0, 0);
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

provide('onPaging', onPaging);

const onCreateDummies = async function(){
    await createDummies()
        .then((response: any)=>{
            if(resStore.isError){
                alert(resStore.getErrorMessage);
                console.log(response)
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

</script>

<style scoped>
@import "../css/contentBase.css";
.select-search{
    width: 65px;
    font-size: 0.8rem;
}

.sort-options {
    display: flex;
    width: 120px;
    flex-wrap: nowrap;
    justify-content: space-between;
}


</style>