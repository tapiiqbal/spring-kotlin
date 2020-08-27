package com.improject.springkotlindemo.controller

import com.improject.springkotlindemo.domain.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/test")
class Testing {
    @GetMapping
    fun getTest(): String = "Testing"


}