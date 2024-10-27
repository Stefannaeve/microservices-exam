import {ApiResponse} from "@/interfaces/ApiResponse";
import {getAssumedSourceType} from "next/dist/build/webpack/loaders/next-flight-loader";
import {Book} from "@/interfaces/Book";

const API_PREFIX = '/api'

export const getBooks = async (): Promise<Array<Book>> => {
    const apiCall = API_PREFIX + '/books'
    const response: ApiResponse<Array<Book>> = await fetch(apiCall).then((res) => res.json())
    console.log("RESPONSE", response)
    if (response.isSuccess && response.object){
        return response.object
    } else {
        console.error(response.errorMessage)
        return null
    }
}