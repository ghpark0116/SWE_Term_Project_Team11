package edu.skku.cs.skkumarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /**
         * 임시 더미 데이터 (추후에 DB에서 받아 올 데이터)
         */
        val products = listOf(
            Product("접이식 테이블", "5,000원", "2024.11.27", R.drawable.ic_launcher_background, "이 테이블은 실내외 어디에서나 유용하게 사용할 수 있는 다목적 테이블입니다. 작은 공간에도 쉽게 설치할 수 있도록 설계되었으며, 사용하지 않을 때는 접어서 보관할 수 있어 공간 활용도를 극대화할 수 있습니다. 가벼운 무게와 내구성 있는 소재를 사용하여 이동이 편리하며, 다양한 환경에서 사용할 수 있도록 디자인되었습니다."),
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

        // RecyclerView Adapter
        val adapter = ProductAdapter(products) { product ->
            // 물품 클릭 시 ProductDetailActivity로 이동
            val intent = Intent(requireContext(), ProductDetailActivity::class.java).apply {
                putExtra("title", product.title)
                putExtra("price", product.price)
                putExtra("date", product.date)
                putExtra("imageResId", product.imageResId)
                putExtra("description", product.description)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter


        // 클릭 시 PostActivity로 이동
        val writePostButton: Button = view.findViewById(R.id.btn_write_post)
        writePostButton.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }

        // 필터 아이콘 클릭
        val filterIcon: ImageView = view.findViewById(R.id.filter_icon)

        filterIcon.setOnClickListener {
            val intent = Intent(requireContext(), FilterActivity::class.java)
            startActivity(intent)
        }

        // 알림 아이콘 클릭
        val notificationIcon: ImageView = view.findViewById(R.id.notification_icon)

        notificationIcon.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        // 검색 아이콘 클릭
        val searchIcon : ImageView = view.findViewById(R.id.search_icon)
        searchIcon.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}

// Product 데이터 클래스
data class Product(
    val title: String,
    val price: String,
    val date: String,
    val imageResId: Int,
    val description: String
)

// RecyclerView Adapter 클래스
class ProductAdapter(private val products: List<Product>, private val onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.product_title)
        val price: TextView = itemView.findViewById(R.id.product_price)
        val date: TextView = itemView.findViewById(R.id.product_date)
        val image: ImageView = itemView.findViewById(R.id.product_image)
    }
}
