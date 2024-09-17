import {FApiResponse} from "@/utils/FApiResponse";
import {ApiResponse} from "@/interfaces/ApiResponse"
import {NextResponse} from "next/server";
import {Book} from "@/interfaces/Book";

export async function GET(): Promise<NextResponse> {

    const path: string = "http://localhost:8080/book/fetchAll";

    const response: Response = await fetch(path);
    //respon
    const data: ApiResponse<Array<Book>> = await response.json();
    console.log("Response", data)
    //return NextResponse<data>;
    return FApiResponse(data)
}