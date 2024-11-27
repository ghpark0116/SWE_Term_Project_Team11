package edu.skku.cs.skkumarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 뒤로 가기 버튼 클릭
        val backIcon: ImageView = findViewById(R.id.search_back_icon)
        backIcon.setOnClickListener {
            finish()
        }

        // 상품 목록 RecyclerView 세팅
        recyclerView = findViewById(R.id.search_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        /**
         * 임시 더미 데이터 (추후에 DB에서 받아 올 데이터)
         */
        val products = listOf(
            Product("접이식 테이블", "5,000원", "2024.11.27", R.drawable.ic_launcher_background, "이 테이블은 실내외 어디에서나 유용하게 사용할 수 있는 다목적 테이블입니다. " +
                    "작은 공간에도 쉽게 설치할 수 있도록 설계되었으며, 사용하지 않을 때는 접어서 보관할 수 있어 공간 활용도를 극대화할 수 있습니다. " +
                    "가벼운 무게와 내구성 있는 소재를 사용하여 이동이 편리하며, 다양한 환경에서 사용할 수 있도록 디자인되었습니다."),
            Product("갈색 코트", "15,000원", "2024.11.26", R.drawable.ic_launcher_background, "코트 상세 설명"),
            Product("운영체제 교과서", "8,000원", "2024.11.25", R.drawable.ic_launcher_background, "교과서 상세 설명"),
            Product("실험복", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복 상세 설명"),
            Product("실험복1", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복1 상세 설명"),
            Product("실험복2", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복2 상세 설명"),
            Product("실험복3", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복3 상세 설명"),
            Product("실험복4", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복4 상세 설명"),
            Product("실험복5", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복5 상세 설명"),
            Product("실험복6", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복6 상세 설명"),
            Product("실험복7", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복7 상세 설명"),
            Product("실험복8", "7,000원", "2024.11.24", R.drawable.ic_launcher_background, "실험복8 상세 설명")
        )

        // 물품 클릭 시 ProductDetailActivity로 이동
        productAdapter = ProductAdapter(products) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("title", product.title)
                putExtra("price", product.price)
                putExtra("date", product.date)
                putExtra("imageResId", product.imageResId)
                putExtra("description", product.description)
            }
            startActivity(intent)
        }
        recyclerView.adapter = productAdapter

        // 물품 검색 로직 (text 변경 할 때 마다 update)
        val searchView: SearchView = findViewById(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredProducts = products.filter { it.title.contains(newText ?: "", true) }
                productAdapter.updateData(filteredProducts)
                return true
            }
        })
    }

    // RecyclerView Adapter
    private inner class ProductAdapter(
        private var products: List<Product>,
        private val onItemClick: (Product) -> Unit
    ) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
            return ProductViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = products[position]
            holder.title.text = product.title
            holder.price.text = product.price
            holder.date.text = product.date
            holder.image.setImageResource(product.imageResId)

            // 아이템 클릭 listener
            holder.itemView.setOnClickListener { onItemClick(product) }
        }

        override fun getItemCount(): Int = products.size

        fun updateData(newProducts: List<Product>) {
            products = newProducts
            notifyDataSetChanged()
        }

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.product_title)
            val price: TextView = itemView.findViewById(R.id.product_price)
            val date: TextView = itemView.findViewById(R.id.product_date)
            val image: ImageView = itemView.findViewById(R.id.product_image)
        }
    }
}

