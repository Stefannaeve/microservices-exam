import {ApiResponse} from "@/utils/ApiResponse";
import {NextResponse} from "next/server";

export async function GET(): Promise<NextResponse> {

    const path: string = "http://localhost:8080/book/fetchAll";

    const response = await fetch(path);
    const data = await response.json();
    console.log("Response", data)
    return ApiResponse(data)
}