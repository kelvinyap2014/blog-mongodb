package io.ky.mb.web.rest;

import io.ky.mb.BlogApp;

import io.ky.mb.domain.BlogEntry;
import io.ky.mb.repository.BlogEntryRepository;
import io.ky.mb.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static io.ky.mb.web.rest.TestUtil.sameInstant;
import static io.ky.mb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlogEntryResource REST controller.
 *
 * @see BlogEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApp.class)
public class BlogEntryResourceIntTest {

    private static final String DEFAULT_BLOG = "AAAAAAAAAA";
    private static final String UPDATED_BLOG = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restBlogEntryMockMvc;

    private BlogEntry blogEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlogEntryResource blogEntryResource = new BlogEntryResource(blogEntryRepository);
        this.restBlogEntryMockMvc = MockMvcBuilders.standaloneSetup(blogEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogEntry createEntity() {
        BlogEntry blogEntry = new BlogEntry()
            .blog(DEFAULT_BLOG)
            .title(DEFAULT_TITLE)
            .user(DEFAULT_USER)
            .content(DEFAULT_CONTENT)
            .date(DEFAULT_DATE);
        return blogEntry;
    }

    @Before
    public void initTest() {
        blogEntryRepository.deleteAll();
        blogEntry = createEntity();
    }

    @Test
    public void createBlogEntry() throws Exception {
        int databaseSizeBeforeCreate = blogEntryRepository.findAll().size();

        // Create the BlogEntry
        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isCreated());

        // Validate the BlogEntry in the database
        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeCreate + 1);
        BlogEntry testBlogEntry = blogEntryList.get(blogEntryList.size() - 1);
        assertThat(testBlogEntry.getBlog()).isEqualTo(DEFAULT_BLOG);
        assertThat(testBlogEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlogEntry.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testBlogEntry.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBlogEntry.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    public void createBlogEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blogEntryRepository.findAll().size();

        // Create the BlogEntry with an existing ID
        blogEntry.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        // Validate the BlogEntry in the database
        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkBlogIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogEntryRepository.findAll().size();
        // set the field null
        blogEntry.setBlog(null);

        // Create the BlogEntry, which fails.

        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogEntryRepository.findAll().size();
        // set the field null
        blogEntry.setTitle(null);

        // Create the BlogEntry, which fails.

        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogEntryRepository.findAll().size();
        // set the field null
        blogEntry.setUser(null);

        // Create the BlogEntry, which fails.

        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogEntryRepository.findAll().size();
        // set the field null
        blogEntry.setContent(null);

        // Create the BlogEntry, which fails.

        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = blogEntryRepository.findAll().size();
        // set the field null
        blogEntry.setDate(null);

        // Create the BlogEntry, which fails.

        restBlogEntryMockMvc.perform(post("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isBadRequest());

        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBlogEntries() throws Exception {
        // Initialize the database
        blogEntryRepository.save(blogEntry);

        // Get all the blogEntryList
        restBlogEntryMockMvc.perform(get("/api/blog-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogEntry.getId())))
            .andExpect(jsonPath("$.[*].blog").value(hasItem(DEFAULT_BLOG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    public void getBlogEntry() throws Exception {
        // Initialize the database
        blogEntryRepository.save(blogEntry);

        // Get the blogEntry
        restBlogEntryMockMvc.perform(get("/api/blog-entries/{id}", blogEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blogEntry.getId()))
            .andExpect(jsonPath("$.blog").value(DEFAULT_BLOG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    public void getNonExistingBlogEntry() throws Exception {
        // Get the blogEntry
        restBlogEntryMockMvc.perform(get("/api/blog-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBlogEntry() throws Exception {
        // Initialize the database
        blogEntryRepository.save(blogEntry);
        int databaseSizeBeforeUpdate = blogEntryRepository.findAll().size();

        // Update the blogEntry
        BlogEntry updatedBlogEntry = blogEntryRepository.findOne(blogEntry.getId());
        updatedBlogEntry
            .blog(UPDATED_BLOG)
            .title(UPDATED_TITLE)
            .user(UPDATED_USER)
            .content(UPDATED_CONTENT)
            .date(UPDATED_DATE);

        restBlogEntryMockMvc.perform(put("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlogEntry)))
            .andExpect(status().isOk());

        // Validate the BlogEntry in the database
        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeUpdate);
        BlogEntry testBlogEntry = blogEntryList.get(blogEntryList.size() - 1);
        assertThat(testBlogEntry.getBlog()).isEqualTo(UPDATED_BLOG);
        assertThat(testBlogEntry.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlogEntry.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testBlogEntry.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBlogEntry.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    public void updateNonExistingBlogEntry() throws Exception {
        int databaseSizeBeforeUpdate = blogEntryRepository.findAll().size();

        // Create the BlogEntry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlogEntryMockMvc.perform(put("/api/blog-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogEntry)))
            .andExpect(status().isCreated());

        // Validate the BlogEntry in the database
        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteBlogEntry() throws Exception {
        // Initialize the database
        blogEntryRepository.save(blogEntry);
        int databaseSizeBeforeDelete = blogEntryRepository.findAll().size();

        // Get the blogEntry
        restBlogEntryMockMvc.perform(delete("/api/blog-entries/{id}", blogEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BlogEntry> blogEntryList = blogEntryRepository.findAll();
        assertThat(blogEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogEntry.class);
        BlogEntry blogEntry1 = new BlogEntry();
        blogEntry1.setId("id1");
        BlogEntry blogEntry2 = new BlogEntry();
        blogEntry2.setId(blogEntry1.getId());
        assertThat(blogEntry1).isEqualTo(blogEntry2);
        blogEntry2.setId("id2");
        assertThat(blogEntry1).isNotEqualTo(blogEntry2);
        blogEntry1.setId(null);
        assertThat(blogEntry1).isNotEqualTo(blogEntry2);
    }
}
