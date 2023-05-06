<template>
  <div class="content d-flex flex-column mt-4">
      <div class="d-flex flex-column ps-3 pb-3 pe-3" style="border-radius: 10px; background-color: #dcdfe6">
          <h3>{{ intro.category }}</h3>
          <span>{{ intro.message }}</span>
      </div>
      <div  class="d-flex mt-4 justify-content-between">
          <div v-if="isNotice">
              <div style="width:180px;"></div>
          </div>
          <div v-else class="d-flex">
              <div>
                  <el-link :href="'/'+route.name+'/new'">
                      <el-button type="primary">
                          <el-icon style="vertical-align: middle">
                              <EditPen />
                          </el-icon>
                          <span style="vertical-align: middle"> 글쓰기 </span>
                      </el-button>
                  </el-link>
              </div>
              <div class="reload-button">
                  <el-button plain @click="reloadArticles()" style="border: none;">
                      <el-icon style="vertical-align: middle">
                          <Refresh />
                      </el-icon>
                  </el-button>
              </div>
          </div>
          <div style="width: 50%;">
              <el-input
                      v-model="search.keyword"
                      placeholder="Please input"
                      class="input-with-select"
                ><template #prepend>
                      <el-select class="select-search" v-model="search.condition" suffix-icon="">
                          <el-option v-for="item in selectList.search" :label="item.text" :value="item.value" />
                      </el-select>
                  </template>
                  <template #append>
                      <el-button plain @click="onSearch()&reloadArticles()" style="border: none;">
                          <el-icon style="vertical-align: middle">
                              <Search />
                          </el-icon>
                      </el-button>
                  </template>
              </el-input>
          </div>
          <div class="sort-options">
              <el-select class="el-select-custom" v-model="sort.period" suffix-icon="" @change="onPeriod()&reloadArticles()">
                  <el-option class="options" v-for="item in selectList.period" :label="item.text" :value="item.value" />
              </el-select>
              <el-select class="el-select-custom" v-model="sort.sort" suffix-icon="" @change="onSort()&reloadArticles()">
                  <el-option class="options" v-for="item in selectList.sort" :label="item.text" :value="item.value" />
              </el-select>
          </div>
      </div>
      <el-divider/>
      <Articles :key="articlesKey"/>
  </div>
</template>

<script setup lang="ts">

import {onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import Articles from "@/components/Articles.vue";

const route = useRoute();
const router = useRouter();

const isNotice = ref(false);
const articlesKey = ref(0);

const sort = ref({
    period:'all',
    sort:'latest',
    isSetPeriod: false,
    isSetSort: false,
})

const search = ref({
    condition:'article',
    keyword:'',
    isSearch: false,
})

const page = ref({
    page:1,
    isSetPage: false,
})

const intro = ref({
    category: '',
    message:'',
})

const selectList = reactive({
    search: [
        {
            text: "제목+글",
            value: "article",
        },
        {
            text: "제목",
            value: "title",
        },
        {
            text: "작성자",
            value: "author",
        }
    ],
    period: [
        {
            text: "ALL",
            value: "all",
        },
        {
            text: "1D",
            value: "1d",
        },
        {
            text: "1W",
            value: "1w",
        },
        {
            text: "1M",
            value: "1m",
        },
        {
            text: "1Y",
            value: "1y",
        },
    ],
    sort: [
        {
            text: "최신순",
            value: "latest",
        },
        {
            text: "조회순",
            value: "view",
        },
        {
            text: "인기순",
            value: "like",
        }
    ],
})

onMounted(()=>{
    setIntro(route.name);
    if(route.name === "notice"){
        isNotice.value = true;
    }
})

const setIntro = function(category: any) {
    switch(category){
        case "community":
            intro.value.category = "커뮤니티";
            intro.value.message = "즐겁게 일상을 나누는 공간입니다.";
            break;
        case "question":
            intro.value.category = "Q&A";
            intro.value.message = "좋은 질문과 답변으로 함께 성장하는 공간입니다.";
            break;
        case "information":
            intro.value.category = "정보공유";
            intro.value.message = "정보를 공유하고 새로운 아이디어를 얻어보세요.";
            break;
        case "notice":
            intro.value.category = "공지사항";
            intro.value.message = "새로운 소식과 공지사항을 전해드리는 공간입니다.";
            break;
        default:
            break;
    }
}


const reloadArticles = function () {
    router.replace(getPath())
    articlesKey.value++;
}

const getPath = function(){
    let isFirst = true;
    let path = '/';

    if(typeof route.name === 'string'){
        path = path+route.name;
    }

    const checkChanges = ref({
        search: null as null | any,
        period: null as null | any,
        sort: null as null | any,
        page: null as null | any,
    })

    if(page.value.isSetPage) checkChanges.value.page = 'page='+page.value.page
    if(search.value.isSearch) checkChanges.value.search = 'condition='+search.value.condition+'&keyword='+search.value.keyword
    if(sort.value.isSetPeriod) checkChanges.value.period = 'period='+sort.value.period
    if(sort.value.isSetSort) checkChanges.value.sort = 'sort='+sort.value.sort

    for (let item in checkChanges) {
        if(item){
            if(isFirst){
                path = path+'?'+item
                isFirst = false
            }else{
                path = path+'&'+item
            }
        }
    }

    return path;
}
const onSearch = function () {
    search.value.isSearch = search.value.keyword !== '';
}

const onSort = function (){
    sort.value.isSetSort = true;
}

const onPeriod = function(){
    sort.value.isSetPeriod = true;
}

</script>

<style scoped>
@import "../components/css/contentBase.css";

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