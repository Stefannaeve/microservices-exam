import {ApiResponse} from "@/interfaces/ApiResponse";
import {getAssumedSourceType} from "next/dist/build/webpack/loaders/next-flight-loader";

const API_PREFIX = '/api'

export const getBooks = async () => {
    const apiCall = API_PREFIX + '/books'
    const response: ApiResponse = await fetch(apiCall).then((res) => res.json())
    console.log("RESPONSE", response)
    return response
}