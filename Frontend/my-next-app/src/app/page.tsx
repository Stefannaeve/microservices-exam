'use client'

import React, {useEffect, useState} from "react";
import {getBooks} from "@/lib/talkToApi"
import {ApiResponse} from "@/interfaces/ApiResponse";
import clsx from 'clsx'
import {Book} from "@/interfaces/Book";


export default function Home() {
    const [something, setSomething] = useState<Book>()

    const findBooks = async () => {
        try {
            const response: Array<Book> | null = await getBooks()
            if (!response){
                return
            }
            console.log("Response", response)
                await addListOfBooks(response)
            console.log("Object", something?.title)
        } catch (error) {
            console.error("Something went wrong")
        }
    }

    const addListOfBooks = async (list: Array<Book>) => {
        list.map((book) => setSomething(book))
    }

    useEffect(() => {
    }, [something])

    const printBooks = () => {
        return <div>{something?.title}</div>
    }

  return (
    <div className="">
      <button className={"w-fit"} onClick={findBooks}>Find books</button>
        <div className={clsx(something ? "": "hidden")}>Hallo</div>
        {printBooks()}
    </div>
  );
}
